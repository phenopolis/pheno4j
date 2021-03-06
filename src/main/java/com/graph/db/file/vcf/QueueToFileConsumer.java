package com.graph.db.file.vcf;

import static com.graph.db.util.Constants.POISON_PILL;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graph.db.file.Parser;
import com.graph.db.output.OutputFileType;
import com.graph.db.util.FileUtil;

public class QueueToFileConsumer implements Runnable {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueToFileConsumer.class);
	
    private final OutputFileType outputFileType;
    private final BlockingQueue<String> queue;
	private final PrintWriter writer;

    public QueueToFileConsumer(OutputFileType outputFileType, BlockingQueue<String> queue, String outputFolder, Class<? extends Parser> clazz) {
        this.outputFileType = outputFileType;
		this.queue = queue;
        
        FileWriter fileWriter = getFileWriter(outputFolder, clazz);
	    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
	    writer = new PrintWriter(bufferedWriter);
    }

	private FileWriter getFileWriter(String outputFolder, Class<? extends Parser> clazz) {
		FileWriter fileWriter;
		try {
			String filePathAndName = FileUtil.createFileName(outputFolder, clazz, outputFileType);
			fileWriter = new FileWriter(filePathAndName, true);
			LOGGER.info("Created fileWriter for: {}", filePathAndName);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return fileWriter;
	}

    @Override
	public void run() {
        try {
			while (true) {
	            Object queueElement = queue.take();

				if (queueElement == POISON_PILL) {
					return;
				}
	            
        	    writer.println(queueElement);
        	    writer.flush();
        	}
        } catch (InterruptedException e) {
			LOGGER.error("Interrupted: ", e);
			Thread.currentThread().interrupt();
        } finally {
        	writer.close();
        }
    }
}