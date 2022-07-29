package lab.square.spltcoverage.core.antenna;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class ProductFeatureLocator {
	
	private ProductFeatureLocator() {
	}

	public static Map<String, Collection<FeatureLocation>> analyze(String srcPath) throws IOException {
		Map<String, Collection<FeatureLocation>> result = new HashMap<>();
		Files.walkFileTree(Paths.get(srcPath), new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String fileName = file.getFileName().toString();
				if (fileName.toLowerCase().endsWith(".java")) {
					FeatureLocator locator = new FeatureLocator();
					result.put(convertToClassName(Paths.get(srcPath), file), locator.analyze(file.toString()));
				}

				return FileVisitResult.CONTINUE;
			}

			private String convertToClassName(Path src, Path javaFile) {
				String withExtension = Paths.get(srcPath)
						.relativize(javaFile)
						.toString()
						.replace('/', '.')
						.replace('\\', '.');
				return withExtension.substring(0, withExtension.lastIndexOf('.'));
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				Logger.getLogger(ProductFeatureLocator.class.getName()).severe(file.toString() + " cannot be read");
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		return result;

	}
}
