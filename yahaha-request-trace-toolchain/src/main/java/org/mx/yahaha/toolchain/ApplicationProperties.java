package org.mx.yahaha.toolchain;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author FizzPu
 * @since 2024/12/4 下午8:22
 */
@Configuration
@ConfigurationProperties(prefix = "yahaha.request.trace.toolchain")
public class ApplicationProperties {
  // @Value("${flame_graph_path}")
  private String flameGraphPath;
  
  public String getFlameGraphPath() {
    if (flameGraphPath != null) {
      return flameGraphPath;
    }
    
    String rootModule = System.getProperty("user.dir");
    return "/Users/fizz/WorkSpace/Codes/CompanyCodes/sensorsdata/yahaha/yahaha-request-trace-toolchain/construction/FlameGraph/flamegraph.pl";
  }
}
