package balon;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Histogram;
import io.prometheus.client.Summary;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.DefaultExports;
import jdk.nashorn.internal.runtime.Context.ThrowErrorManager;

public class Boot {

    private static final Logger log = LoggerFactory.getLogger(Boot.class);
    
    private static final List<Counter> counters = new ArrayList<>();
    private static final List<Gauge> gauges = new ArrayList<>();
    private static final List<Histogram> histograms = new ArrayList<>();
    private static final List<Summary> summaries = new ArrayList<>();    

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);
    private static final Random r = new Random();

    public static void main(String[] args) throws Exception {
        
        // Prometheus JVM Hotspot metrics default exports initialization
        DefaultExports.initialize();
        
        int metricsCount = metricsCount();
        log.debug("Metrics count: "+ metricsCount);
        prepareAppMetrics(metricsCount);

        scheduler.scheduleAtFixedRate(Boot::incrementMetrics, 0, 10, TimeUnit.SECONDS);

        Server server = new Server(8080);
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(MetricsServlet.class, "/metrics");
        server.start();
        server.dumpStdErr();
        server.join();
        
        
    }

    private static void incrementMetrics() {
        try {
            counters.forEach(c->c.labels("value1").inc(r.nextInt(5)));
            gauges.forEach(g->g.labels("value1").inc(r.nextInt(3)));
            histograms.forEach(h->h.labels("value2").observe(r.nextInt(5)));
            summaries.forEach(s->s.labels("value1").observe(r.nextInt(3)));
        } catch (Throwable thr) {
            log.error("Increment metrics error", thr);
        }
    }

    private static int metricsCount() {
        String env = System.getenv("APP_METRICS_COUNT");
        return env!=null ? Integer.parseInt(env) : 1;
    }
    
    private static void prepareAppMetrics(int count) {
        
        for(int i=0; i<count; i++) {
            
            counters.add(Counter.build()
                .namespace("test_java_app_nmsp")
                .subsystem("subs")
                .name("counter_" + i + "_total")
                .help("Custom counter metric no. " + i)
                .labelNames("label1")
                .register());

            gauges.add(Gauge.build()
                .namespace("test_java_app_nmsp")
                .subsystem("subs")
                .name("gauge_" + i + "_current")
                .help("Custom gauge metric no. " + i)
                .labelNames("label1")
                .register());

            histograms.add(Histogram.build()
                .namespace("test_java_app_nmsp")
                .subsystem("subs")
                .name("histogram_" + i + "_bytes")
                .help("Custom histogram metric no. " + i)
                .labelNames("label1")
                .register());

            summaries.add(Summary.build()
                .namespace("test_java_app_nmsp")
                .subsystem("subs")
                .name("summary_" + i + "_bytes")
                .help("Custom summary metric no. " + i)
                .labelNames("label1")
                .register());
            
        }
        
    }
    
}
