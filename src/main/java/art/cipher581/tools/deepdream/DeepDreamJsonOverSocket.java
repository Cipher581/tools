package art.cipher581.tools.deepdream;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class DeepDreamJsonOverSocket implements IDeepDream {

	private Socket s;

	private BufferedWriter out;

	private BufferedReader in;

	private final String host;

	private final int port;


	public DeepDreamJsonOverSocket(String host, int port) {
		super();

		if (host == null) {
			throw new IllegalArgumentException("host is null");
		}

		this.host = host;
		this.port = port;
	}


	@Override
	public void run(File file, File targetFile, DeepDreamSettings settings) throws DeepDreamException {
		try {
			if (s == null || s.isClosed()) {
				connect();
			}
			Request request = new Request(file, targetFile, settings);

			ObjectMapper objectMapper = new ObjectMapper();
			String requestStr = objectMapper.writeValueAsString(request);
			requestStr = requestStr.replaceAll("\n", "").replaceAll("\r", "");

			System.out.println("sending request: " + requestStr);

			out.write(requestStr);
			out.write("\n");
			out.flush();

			String response = in.readLine();

			if (response == null || !response.toLowerCase().equals("ok")) {
				throw new DeepDreamException("unexpected response: " + response);
			}
		} catch (IOException ex) {
			throw new DeepDreamException("io-error during deep dream of " + file, ex);
		}
	}


	private void connect() throws IOException, UnknownHostException {
		this.s = SocketFactory.getDefault().createSocket(host, port);

		this.out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
	}

	@SuppressWarnings("unused")
	private static class Request implements Serializable {

		private static final long serialVersionUID = 1218935128681000363L;

		private final String file;

		private final String targetFile;

		private final DeepDreamSettings settings;


		public Request(File file, File targetFile, DeepDreamSettings settings) {
			this.file = file.getAbsolutePath();
			this.targetFile = targetFile.getAbsolutePath();
			this.settings = settings;
		}


		public String getFile() {
			return file;
		}


		public String getTargetFile() {
			return targetFile;
		}


		public DeepDreamSettings getSettings() {
			return settings;
		}

	}

}
