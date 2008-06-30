import javax.jcr._;
import java.util._;
import org.apache.jackrabbit._;

	def id[T](x : T) = x;

	var bi = bindings.keySet().iterator();
	var a = bi.next();
	var b = bi.next();
	var c = bi.next();
	var d = bi.next();
	var e = bi.next();
	var f = bi.next();
	var g = bindings.get(f);
	var h = g.asInstanceOf(Node);
