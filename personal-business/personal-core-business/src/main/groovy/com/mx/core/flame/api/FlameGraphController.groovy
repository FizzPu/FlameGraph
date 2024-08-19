package com.mx.core.flame.api;

import com.frontend.ui.FlameGraphIndex
import com.frontend.ui.UiComponent
import com.mx.core.flame.service.FlameGraphService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

/**
 * @author FizzPu
 * @since 2024/4/7 21:19
 */
@Controller
class FlameGraphController {
	private final FlameGraphService flameGraphService;
	private final FlameGraphIndex flameGraphPage = new FlameGraphIndex()

	FlameGraphController(FlameGraphService flameGraphService) {
		this.flameGraphService = flameGraphService
	}

	@GetMapping(value = "/flamegraph/logs")
	UiComponent flamePage() {
		return flameGraphPage
	}

	@PostMapping(value = "/flamegraph/logs")
	UiComponent parseLogs(@RequestParam String logs) {
		return flameGraphService.parseLogs(logs)
	}

	@GetMapping(value = "/flamegraph/history")
	UiComponent getFlameHistory() {
		return flameGraphService.queryFlameGraphList()
	}

	@GetMapping(value = "/flamegraph/detail")
	UiComponent getDetail(String taskId) {
		return flameGraphService.getGraphDetail(taskId)
	}
}
