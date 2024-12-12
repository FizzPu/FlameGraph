package org.mx.yahaha.toolchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author FizzPu
 * @since 2024/12/4 下午8:22
 */
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationProperties {
  @Value("${yahaha.request.trace.toolchain.flame_graph_path}")
  private String flameGraphPath;
  
  public String getFlameGraphPath() {
    return flameGraphPath;
  }
  
  public void setFlameGraphPath(String flameGraphPath) {
    this.flameGraphPath = flameGraphPath;
  }
}
