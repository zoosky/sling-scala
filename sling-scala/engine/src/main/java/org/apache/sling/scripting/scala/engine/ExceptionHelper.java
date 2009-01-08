package org.apache.sling.scripting.scala.engine;

public final class ExceptionHelper {

    private ExceptionHelper() {
        super();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Throwable> T initCause(T throwable, Throwable cause) {
        return (T) throwable.initCause(cause);
    }

}
