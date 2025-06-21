package org.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = {
        "org.common.eventing.core"
})
public class EventBusAutoConfiguration {
}
