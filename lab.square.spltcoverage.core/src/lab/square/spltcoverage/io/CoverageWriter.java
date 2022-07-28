package lab.square.spltcoverage.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataReader;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.ExecutionDataWriter;
import org.jacoco.core.data.SessionInfo;
import org.jacoco.core.data.SessionInfoStore;

public class CoverageWriter {
	
	public static void makeExecFile(String directory, final byte[] exeData) throws IOException {
		Files.createDirectories(Paths.get(directory).getParent());
		String fileName = directory + ".exec";
		final OutputStream fileStream = Files.newOutputStream(Paths.get(fileName), StandardOpenOption.CREATE_NEW);
		ExecutionDataWriter writer = new ExecutionDataWriter(fileStream);

		final ExecutionDataStore execStore = new ExecutionDataStore();
		final SessionInfoStore sessionStore = new SessionInfoStore();

		final ExecutionDataReader reader = new ExecutionDataReader(new ByteArrayInputStream(exeData));
		reader.setExecutionDataVisitor(execStore);
		reader.setSessionInfoVisitor(sessionStore);
		reader.read();

		for (ExecutionData data : execStore.getContents()) {
			writer.visitClassExecution(data);
		}
		for (SessionInfo info : sessionStore.getInfos()) {
			writer.visitSessionInfo(info);
		}

		writer.flush();
		fileStream.close();
	}

	private static void makeDirectory(String directory) {
		String[] splitted = directory.split("/");
		String checkDirectory = "";
		for (int i = 0; i < splitted.length - 1; i++) {
			checkDirectory = checkDirectory + splitted[i] + "/";
			File file = new File(checkDirectory);
			if (!file.exists())
				file.mkdir();
		}
	}
}
