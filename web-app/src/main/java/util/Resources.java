package util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import org.teavm.jso.JSBody;
import org.teavm.jso.JSFunctor;
import org.teavm.jso.JSObject;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLImageElement;
import org.teavm.metaprogramming.CompileTime;
import org.teavm.metaprogramming.Meta;
import org.teavm.metaprogramming.Metaprogramming;
import org.teavm.metaprogramming.Value;

import shim.Io;
import shim.Shim;
import shim.awt.image.BufferedImage;

public class Resources {
	private static int progressTotal = 0, progressTodo = 0;
	private static HTMLElement progressElement;
	private static final Map<String, String> TEXT_RESOURCES = new HashMap<>();
	private static final Map<String, HTMLImageElement> IMG_RESOURCES = new HashMap<>();

	private static <T> T TODO(String msg) {
		System.out.println("TODO: " + msg);
		return null;
	}

	public static String loadTextResource(String resourceName) throws IOException {
		var text = Objects.requireNonNull(TEXT_RESOURCES.get(resourceName), resourceName);
		return text;
	}

	public static String loadTextFile(String path) throws IOException {
		return TODO("loadTextFile" + path);
	}

	public static BufferedImage readImageResourceUnfliped(String resourceName) throws IOException {
		var img = Objects.requireNonNull(IMG_RESOURCES.get(resourceName), resourceName);
		return BufferedImage.shimBufferedImage(img);
	}

	// NOTE: defined in the original class, but unused
	@SuppressWarnings("unused")
	private static BufferedImage readImageResourceFliped(String resourceName) throws IOException {
		throw new UnsupportedOperationException();
	}

	public static URL getResourceURL(String resourceName) {
		return TODO("getResourceURL" + resourceName);
	}

	public static Io<Void> shimInitResources() {
		return Io.par0(par -> {
			ResourcesMeta.listResources(resourcePath -> {
				String resource = "resources/" + resourcePath;
				if (resource.endsWith(".png") || resource.endsWith(".jpg"))
					par.run(
						progress(resource, loadImg(resource))
							.map(img -> {
								IMG_RESOURCES.put("/" + resource, img);
								return null;
							})
					);
				else
					par.run(
						progress(resource, fetchString(resource))
							.map(text -> {
								TEXT_RESOURCES.put("/" + resource,  text);
								return null;
							})
					);
			});
		});
	}

	@CompileTime
	private static class ResourcesMeta {
		@Meta
		private static native void listResources(Consumer<String> c);
		private static void listResources(Value<Consumer<String>> c) throws IOException {
			var resourcesPath = Paths.get(ResourcesMeta.class.getResource("/resources").getFile());
			try (var s = Files.walk(resourcesPath)) {
				s.forEach(path -> {
					if (!Files.isRegularFile(path))
						return;
					String resourcePath = resourcesPath.relativize(path).toString();
					Metaprogramming.emit(() -> c.get().accept(resourcePath));
				});
			}
		}
	}

	@JSFunctor
	private static interface StringCallback extends JSObject {
		void accept(String val);
	}

	@JSFunctor
	private static interface UnknownCallback extends JSObject {
		void accept(JSObject val);
	}

	private static Io<String> fetchString(String url) {
		return (onResolve, onReject) -> fetchString(url, onResolve::accept, e -> onReject.accept(new RuntimeException("fetch: " + e)));
	}

	@JSBody(params = { "url", "onResolve", "onReject" }, script = "fetch(url, { mode: \"no-cors\" }).then(function(resp) { return resp.text(); }).then(function(text) { onResolve(text); }, function(err) { onReject(err); });" )
	private static native void fetchString(String url, StringCallback onResolve, UnknownCallback onReject);

	private static Io<HTMLImageElement> loadImg(String url) {
		return (onResolve, onReject) -> {
			var img = (HTMLImageElement) Shim.document().createElement("img");
			img.setSrc(url);
			var o = new Object() {
				private boolean done;
				private boolean setDone() {
					if (done)
						return false;
					return done = true;
				}
			};
			img.addEventListener("load", e -> {
				if (o.setDone())
					onResolve.accept(img);
			});
			img.addEventListener("load", e -> {
				if (o.setDone())
					onReject.accept(new RuntimeException("img: " + e));
			});
		};
	}

	private static void progressChange(int n) {
		progressTodo += n;
		if (n > 0)
			progressTotal += n;
		if (progressTodo == 0) {
			if (progressElement != null) {
				progressElement.getParentNode().removeChild(progressElement);
				progressElement = null;
			}
		} else {
			if (progressElement == null)
				Shim.document().getBody().appendChild(progressElement = Shim.document().createElement("div"));
			progressElement.setInnerHTML("Progress: " + (progressTotal - progressTodo) + "/" + progressTotal);
		}
	}

	private static <T> Io<T> progress(String label, Io<T> io) {
		return Io.defer(() -> {
			progressChange(1);
			var elem = Shim.document().getBody().appendChild(Shim.document().createElement("div"));
			elem.appendChild(Shim.document().createTextNode("Loading " + label + "..."));
			return io.map(v -> {
				progressChange(-1);
				elem.getParentNode().removeChild(elem);
				return v;
			});
		});
	}
}
