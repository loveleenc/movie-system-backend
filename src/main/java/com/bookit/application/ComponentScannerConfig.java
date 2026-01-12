package com.bookit.application;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Component
@ComponentScan(basePackages = {"com.bookit.security", "com.bookit.venue.theatre", "com.bookit.events.shows"})
public class ComponentScannerConfig {
}
