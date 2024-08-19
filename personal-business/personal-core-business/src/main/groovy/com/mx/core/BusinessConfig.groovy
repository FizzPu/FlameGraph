package com.mx.core


import org.springframework.stereotype.Component
/**
 * @author FizzPu
 * @since 2024/6/25 下午7:37
 */
@Component
class BusinessConfig {
  private String flameGraphPath = "/Users/FizzPu/Desktop/FizzALL/efficient_tool/FlameGraph/flamegraph.pl"

  String getFlameGraphPath() {
     return System.getProperty("flameGraphPath", flameGraphPath)
  }
}
