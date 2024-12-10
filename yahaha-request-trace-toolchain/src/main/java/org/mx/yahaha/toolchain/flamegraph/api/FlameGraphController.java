package org.mx.yahaha.toolchain.flamegraph.api;

import org.mx.yahaha.toolchain.flamegraph.service.FlameGraphService;
import org.mx.yahaha.toolchain.ui.UiComponent;
import org.mx.yahaha.toolchain.ui.flamegraph.FlameGraphIndex;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author FizzPu
 * @since 2024/4/7 21:19
 */
@Controller
class FlameGraphController {
	private final FlameGraphService flameGraphService;
	private final FlameGraphIndex flameGraphPage = new FlameGraphIndex();

	FlameGraphController(FlameGraphService flameGraphService) {
		this.flameGraphService = flameGraphService;
	}

	@GetMapping(value = "/flamegraph/logs")
	public UiComponent flamePage() {
		return flameGraphPage;
	}

	@PostMapping(value = "/flamegraph/logs")
	public UiComponent parseLogs(@RequestParam String logs) {
		return flameGraphService.parseLogs(logs);
	}

	@GetMapping(value = "/flamegraph/history")
	public UiComponent getFlameHistory() {
		return flameGraphService.queryFlameGraphList();
	}

	@GetMapping(value = "/flamegraph/detail")
	public UiComponent getDetail(@RequestParam("task_id") String taskId) {
		return flameGraphService.getGraphDetail(taskId);
	}
}
