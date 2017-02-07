from neo4j.v1 import GraphDatabase, basic_auth
import SocketServer
import SimpleHTTPServer
import json
import hashlib
from bson.json_util import dumps


driver = GraphDatabase.driver("bolt://localhost:57687", auth=basic_auth("neo4j", "1"))

PORT = 9000

def patients(args):
  session = driver.session()
  result = session.run("MATCH (a:Person) return a.personId as personId ")
  s=[]
  for record in result:
      s+=["%s" % (record["personId"])]
      session.close()
  return '\n'.join(s)

def shared_variants(args):
    print(args)
    q=""" WITH ["{person1}","{person2}"] as persons
    MATCH (p:Person)<-[:GeneticVariantToPerson]-(v:GeneticVariant) 
    WHERE p.personId IN persons
    WITH v, count(*) as c, persons
    WHERE c = size(persons)
    RETURN count(v.variantId);
    """.format(person1=args['person1'],person2=args['person2'])
    session = driver.session()
    result = session.run(q)
    session.close()
    return json.dumps([r.__dict__ for r in result], indent=4)


def rv_sharing(args):
    individual_id=args.get('individual_id','')
    thresh=args.get('thresh',0.01)
    allele_freq=args.get('allele_freq',0.01)
    limit=args.get('limit',10)
    #thresh=0.05
    #allele_freq=0.001
    print individual_id
    print float(thresh)
    print float(allele_freq)
    q= """ MATCH (k:Person)
    WITH count(k) as numberOfPeople
    MATCH (p:Person {{personId:"{personId}"}})<-[:PRESENT_IN]-(gv:GeneticVariant)
    WHERE (gv.allele_freq < {allele_freq} or gv.hasExac = false)
    WITH size(()<-[:PRESENT_IN]-(gv)) as count , gv, p, numberOfPeople
    WHERE count > 1
    AND ((count / toFloat(numberOfPeople))  <= {thresh})
    MATCH (gv)-[:PRESENT_IN]->(q:Person)
    WHERE p <> q
    WITH p,q,count(gv) as intersection, numberOfPeople
    ORDER BY intersection DESC limit {limit}
    MATCH (x:Person)<-[:PRESENT_IN]-(v:GeneticVariant)
    WHERE (x.personId = p.personId or x.personId = q.personId)
    AND (v.allele_freq < {allele_freq} or v.hasExac = false)
    AND ((size(()<-[:PRESENT_IN]-(v)) / toFloat(numberOfPeople))  <= {thresh})
    WITH p, q, v, intersection
    RETURN p.personId, q.personId, intersection, size(collect(distinct v)) as unionSum, (round((intersection/toFloat(size(collect(distinct v))))*100.0*10)/10) as PercentShared
    ORDER BY PercentShared DESC;
    """.format(thresh=float(thresh), personId=individual_id,allele_freq=float(allele_freq),limit=int(limit))
    session = driver.session()
    result = session.run(q)
    session.close()
    return json.dumps([r.__dict__ for r in result], indent=4)


handlers={'/patients':patients,'/rv_sharing':rv_sharing,'/shared_variants':shared_variants}

class CustomHandler(SimpleHTTPServer.SimpleHTTPRequestHandler):
      def do_GET(self):
         #Sample values in self for URL: http://localhost:9090/jsxmlrpc-0.3/
         #self.path  '/jsxmlrpc-0.3/'
         #self.raw_requestline   'GET /jsxmlrpc-0.3/ HTTP/1.1rn'
         #self.client_address    ('127.0.0.1', 3727)
         path=self.path
         args=dict()
         if '?' in path:
            path,args,=path.split('?')
            args=dict([a.split('=') for a in args.split('&')])
            print path, args,
         self.send_response(200)
         self.send_header('Content-type','text/html')
         self.end_headers()
         #call sample function here
         self.wfile.write(handlers[path](args))

httpd = SocketServer.ThreadingTCPServer(('localhost', PORT),CustomHandler)

print "serving at port", PORT
httpd.serve_forever()

print(session)


