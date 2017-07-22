package com.justjournal;

import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * @author Lucas Holt
 */
public class TrampolineSchedulerRule implements TestRule {

    @Override
    public Statement apply(final Statement base, Description d) {
      return new Statement() {
        @Override
        public void evaluate() throws Throwable {
            RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
                @Override
                public Scheduler apply(final Scheduler scheduler) throws Exception {
                    return Schedulers.trampoline();
                }
            });
            RxJavaPlugins.setComputationSchedulerHandler(new Function<Scheduler, Scheduler>() {
                             @Override
                             public Scheduler apply(final Scheduler scheduler) throws Exception {
                                 return Schedulers.trampoline();
                             }
                         });
            RxJavaPlugins.setNewThreadSchedulerHandler(new Function<Scheduler, Scheduler>() {
                                          @Override
                                          public Scheduler apply(final Scheduler scheduler) throws Exception {
                                              return Schedulers.trampoline();
                                          }
                                      });
            

          try {
            base.evaluate();
          } finally {
            RxJavaPlugins.reset();
          }
        }
      };
    }
  }