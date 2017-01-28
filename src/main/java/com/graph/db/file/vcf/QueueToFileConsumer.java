package com.graph.db.file.vcf;

import static com.graph.db.util.Constants.POISON_PILL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.output.OutputFileType;
import com.graph.db.util.FileUtil;

public class QueueToFileConsumer implements Runnable {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueToFileConsumer.class);
	
    private final BlockingQueue<String> queue;
	private final PrintWriter writer;

    public QueueToFileConsumer(BlockingQueue<String> queue, String outputFolder, Class<? extends VcfParser> clazz) {
        this.queue = queue;
        
        FileWriter fileWriter = getFileWriter(outputFolder, clazz);
	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    writer = new PrintWriter(bufferedWriter);
    }

	private FileWriter getFileWriter(String outputFolder, Class<? extends VcfParser> clazz) {
		FileWriter fileWriter;
		try {
			String filePathAndName = FileUtil.createFileName(outputFolder, clazz, OutputFileType.GENETIC_VARIANT_TO_PERSON);
			fileWriter = new FileWriter(filePathAndName, true);
			LOGGER.info("Created fileWriter for: {}", filePathAndName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileWriter;
	}

    @Override
	public void run() {
    	boolean swallowed = false;
        try {
			while (!swallowed) {
	            Object queueElement = queue.take();

				if (queueElement == POISON_PILL) {
					swallowed = true;
					return;
				}
	            
        	    writer.println(queueElement);
        	    writer.flush();
        	}
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
        	writer.close();
        }
    }
}