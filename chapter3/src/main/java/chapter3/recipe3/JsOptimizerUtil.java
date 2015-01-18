package chapter3.recipe3;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;

/**
 * Google Closure compiler Util
 * 
 * @author Trieu.nguyen
 *
 */
public class JsOptimizerUtil {
	/**
	 * @param code
	 *            JavaScript source code to compile.
	 * @return The compiled version of the code.
	 */
	public static String compile(String code) {
		
//		System.out.println("------------------------");
//		System.out.println(code);
//		System.out.println("------------------------");
		
		Compiler compiler = new Compiler();

		CompilerOptions options = new CompilerOptions();
		// Advanced mode is used here, but additional options could be set, too.
		CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);

		// To get the complete set of externs, the logic in
		// CompilerRunner.getDefaultExterns() should be used here.
		SourceFile extern = SourceFile.fromCode("externs.js", "");

		// The dummy input name "input.js" is used here so that any warnings or
		// errors will cite line numbers in terms of input.js.
		SourceFile input = SourceFile.fromCode("input.js", code);

		// compile() returns a Result, but it is not needed here.
		compiler.compile(extern, input, options);

		// The compiler is responsible for generating the compiled code; it is
		// not
		// accessible via the Result.
		return compiler.toSource();
	}
}
