package shim;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.browser.AnimationFrameCallback;
import org.teavm.jso.browser.Window;
import org.teavm.jso.core.JSArray;
import org.teavm.jso.dom.html.HTMLDocument;

public class Shim {
	public static HTMLDocument document() {
		return window().getDocument();
	}

	public static Window window() {
		return Window.current();
	}

	public static void requestAnimationFrame(AnimationFrameCallback callback) {
		Window.requestAnimationFrame(callback);
	}

	public static void log(JSObject... data) {
		log(JSArray.of(data));
	}

	// TODO: see if this already happens with `a == b`
	@JSBody(params = { "a", "b" }, script = "return a === b;")
	public static native boolean equals(JSObject a, JSObject b);

	@JSBody(params = { "args" }, script = "console.log.apply(console, args);")
	private static native void log(JSArray<JSObject> args);

	// NOTE: unused
	public static String readAllBytesToString(InputStream is) throws IOException {
		Objects.requireNonNull(is, "is");
		// TODO: upgrade to a newer TeaVM that implements `InputStream.readAllBytes`?
		return readAllCharsToString(new InputStreamReader(is, StandardCharsets.UTF_8));
	}

	public static String readAllCharsToString(Reader r) throws IOException {
		Objects.requireNonNull(r, "r");
		var buffer = new char[4096];
		var sb = new StringBuilder();
		for (;;) {
			int size = r.read(buffer);
			if (size < 0)
				break;
			sb.append(buffer, 0, size);
		}
		return sb.toString();
	}
}
