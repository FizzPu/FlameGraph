package org.mx.yahaha.toolchain.flamegraph.service

import org.mx.yahaha.toolchain.ApplicationProperties
import org.mx.yahaha.toolchain.flamegraph.repository.FlameGraphRepository
import org.mx.yahaha.toolchain.ui.UiComponent
import org.mx.yahaha.toolchain.ui.flamegraph.FlameGraphDetail;
import org.mx.yahaha.toolchain.ui.flamegraph.FlameGraphDetailList;
import org.mx.yahaha.toolchain.ui.flamegraph.FlameGraphImage;
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.util.concurrent.CountDownLatch

/**
 * @author FizzPu
 * @since 2024/4/18 11:12
 */
@Service
class FlameGraphService {
	private static final Logger logger = LoggerFactory.getLogger(FlameGraphService.class);

	private final String flameGraphPath
  private final FlameGraphRepository flameGraphRepository
  private final FlameGraphLogsClipper flameGraphLogsClipper

	FlameGraphService(ApplicationProperties businessConfig, FlameGraphRepository flameGraphRepository,
		FlameGraphLogsClipper flameGraphLogsClipper) {
		this.flameGraphRepository = flameGraphRepository;
		this.flameGraphLogsClipper = flameGraphLogsClipper;
		this.flameGraphPath = businessConfig.getFlameGraphPath();
	}

	UiComponent getGraphDetail(String taskId) {
		FlameGraphLogsClipper.FlameGraphLogEntry flameGraphLogEntry = flameGraphRepository.queryByLogId(taskId)
			.orElseThrow(() -> new IllegalArgumentException("invalid taskId."));
    String stackTrace = flameGraphLogEntry.getStackTrace();

		// 1. 创建临时文件, 写入采集到的堆栈
    String tmpFileName = UUID.randomUUID().toString();
		File file = File.createTempFile(tmpFileName, ".stack");
		try {
			file.write(stackTrace)
		} catch (IOException ioException) {
			logger.error("error occurred when writing file", ioException)
			try {
				file.delete()
			} catch (IOException err) {
				logger.warn("unexpected error when delete file", err)
			}
			throw ioException
		}

		//2. 执行perl命令
		def absolutePath = file.getAbsolutePath()
    def command = "perl ${flameGraphPath} ${absolutePath}"
    Process process = command.execute()

		// 3. 读取缓冲区
    def output= new StringBuilder()
		def errOutput = new StringBuilder()
		readProcessBuffer(output, process.inputStream)
		readProcessBuffer(errOutput, process.errorStream)
    process.waitFor()

		// 4. 删除临时文件
    file.delete()

		if (errOutput.length() > 0) {
			logger.error("error occurred when executing perl command. [errorMsg = {}]", errOutput.toString())
		}
    return new FlameGraphImage(output.toString())
  }

	FlameGraphDetailList parseLogs(String logs) {
		FlameGraphDetailList flameGraphDetailList = new FlameGraphDetailList()
		Collection<FlameGraphLogsClipper.FlameGraphLogEntry> flameGraphLogs = flameGraphLogsClipper.clip(logs)

		for (FlameGraphLogsClipper.FlameGraphLogEntry logEntry : flameGraphLogs) {
			String taskInfo = logEntry.getTaskInfo()
			String taskContext = logEntry.getTaskContext()
			FlameGraphDetail flameGraphDetail = new FlameGraphDetail(taskInfo, taskContext)
			flameGraphDetailList.add(flameGraphDetail)

			String taskId = flameGraphDetail.getTaskInfo().taskId

			flameGraphRepository.save(taskId, logEntry)
		}

		return flameGraphDetailList
	}

	FlameGraphDetailList queryFlameGraphList() {
		FlameGraphDetailList flameGraphDetailList = new FlameGraphDetailList()
		Collection<FlameGraphLogsClipper.FlameGraphLogEntry> flameGraphLogs = flameGraphRepository.queryList()

		for (FlameGraphLogsClipper.FlameGraphLogEntry logEntry : flameGraphLogs) {
			String taskInfo = logEntry.getTaskInfo()
			String taskContext = logEntry.getTaskContext()
			FlameGraphDetail flameGraphDetail = new FlameGraphDetail(taskInfo, taskContext)
			flameGraphDetailList.add(flameGraphDetail)
		}

		return flameGraphDetailList
	}

	private static void readProcessBuffer(StringBuilder stringBuilder, InputStream inputStream) {
		def outputCountDownLatch = new CountDownLatch(1)
		new Thread(new TextDumper(inputStream, stringBuilder, outputCountDownLatch)).start()
		outputCountDownLatch.await()
	}

	private static class TextDumper implements Runnable {
		final Appendable app
		final InputStream inputStream
		final CountDownLatch countDownLatch

    TextDumper(InputStream inputStream, Appendable app, CountDownLatch countDownLatch) {
			this.app = app
			this.inputStream = inputStream
			this.countDownLatch = countDownLatch
		}

    void run() {
			InputStreamReader isr = new InputStreamReader(this.inputStream)
			BufferedReader br = new BufferedReader(isr)

			try {
				String next
				while((next = br.readLine()) != null) {
					if (this.app != null) {
						this.app.append(next)
						this.app.append("\n")
					}
				}

			} catch (IOException var5) {
				IOException e = var5
				throw new GroovyRuntimeException("exception while reading process stream", e)
			} finally {
				countDownLatch.countDown()
			}
		}
	}
}
