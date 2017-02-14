package com.graph.db.file.term.subscriber;

import static com.graph.db.util.Constants.HYPHEN;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import com.graph.db.domain.input.term.RawTerm;
import com.graph.db.domain.output.TermToDescendantTermsOutput;
import com.graph.db.file.SetBasedGenericSubscriber;
import com.graph.db.output.OutputFileType;

public class TermToDescendantTermsSubscriber extends SetBasedGenericSubscriber<RawTerm, RawTerm> {
	
	public TermToDescendantTermsSubscriber(String outputFolder, Class<?> parserClass) {
		super(outputFolder, parserClass, OutputFileType.TERM_TO_DESCENDANT_TERMS);
	}
	
	@Override
	public void processRow(RawTerm rawTerm) {
		set.add(rawTerm);
	}
	
	@Override
	public void preClose() {
		DirectedGraph<String, String> graph = new SimpleDirectedGraph<>(String.class);
		addVerticesToGraph(graph);
		addEdgesToGraph(graph);
		
		depthFirstSearchAndWriteOutResults(graph);
	}

	private void addVerticesToGraph(DirectedGraph<String, String> graph) {
		for (RawTerm rawTerm : set) {
			graph.addVertex(rawTerm.getTermId());
		}
	}

	private void addEdgesToGraph(DirectedGraph<String, String> graph) {
		for (RawTerm rawTerm : set) {
			for (String isA : rawTerm.getIsA()) {
				graph.addEdge(isA, rawTerm.getTermId(), isA + HYPHEN + rawTerm.getTermId());
			}
		}
	}
	
	private void depthFirstSearchAndWriteOutResults(DirectedGraph<String, String> graph) {
		for (RawTerm rawTerm : set) {
			DepthFirstIterator<String, String> depthFirstIterator = new DepthFirstIterator<>(graph, rawTerm.getTermId());
			
			while (depthFirstIterator.hasNext()) {
				String element = depthFirstIterator.next();
				write(new TermToDescendantTermsOutput(rawTerm.getTermId(), element));
			}
		}
	}
}
