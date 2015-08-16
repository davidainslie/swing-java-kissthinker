package com.kissthinker.swing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.swing.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kissthinker.boot.Bootstrap.Bootstrapper;
import com.kissthinker.exception.StackTraceUtil;
import com.kissthinker.function.Function;
import com.kissthinker.object.Type;
import com.kissthinker.system.Environment;
import static com.kissthinker.function.Fn.from;
import static com.kissthinker.function.Fn.function;

/**
 * Bootstrap GUI classes such as those with a "main" method may be "marked up" by this annotation to allow for "automatic initialisation".
 * <br/>
 * Unlike {@link com.kissthinker.boot.Bootstrap}, {@link Bootstrap} should not be used with non GUI bootstrapping such as unit tests.<br/>
 * @see com.kissthinker.boot.Bootstrap
 * @author David Ainslie
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Bootstrap
{
    /** */
    public Environment environment() default Environment.INTEGRATION;

    /**
     * Bootstrap through this class/aspect.
     * <br/>
     * @author David Ainslie
     *
     */
    @Aspect
    public static class SwingBootstrapper
    {
        /** */
        private static final Logger LOGGER = LoggerFactory.getLogger(SwingBootstrapper.class);

        /**
         *
         * @param joinPoint
         */
        @Before("within(@com.kissthinker.swing.Bootstrap *) && @annotation(bootstrap)")
        public void bootstrap(JoinPoint joinPoint, Bootstrap bootstrap)
        {
            Bootstrapper.bootstrap(bootstrap.environment(), function(from(this).setupEnvironment()));
        }

        /**
         * Setup the environment such as registering "hooks".
         * @return Void to allow this method to be wrapped up in a {@link Function}
         */
        public Void setupEnvironment()
        {
            Thread.setDefaultUncaughtExceptionHandler((thread, t) -> {
                LOGGER.error("On thread {} caught exception {}", thread, StackTraceUtil.toString(t));
                JOptionPane.showMessageDialog(WindowUtil.activeFrame(), "Error: " + t.toString(), "Error", JOptionPane.ERROR_MESSAGE);
            });

            return Type.VOID;
        }
    }
}