package lab.square.spltcoverage.model.antenna;

import java.io.File;
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

import lab.square.spltcoverage.core.antenna.FeatureLocator;

public class ProductFeatureLocation {
	private static Logger logger = Logger.getLogger(ProductFeatureLocation.class.getName());
	
	private Map<String, Collection<FeatureLocation>> productFeatureLocations;
	
	public ProductFeatureLocation(String javaSourcePath) throws IOException {
		productFeatureLocations = new HashMap<>();
		Path absoluteJavaSourcePath = Paths.get(javaSourcePath).toAbsolutePath();
		Files.walkFileTree(Paths.get(javaSourcePath).toAbsolutePath(), new FileVisitor<Path>() {

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				String fileName = file.getFileName().toString();
				if (fileName.toLowerCase().endsWith(".java")) {
					FeatureLocator locator = new FeatureLocator();
					productFeatureLocations.put(convertToClassName(absoluteJavaSourcePath, file), locator.analyze(file.toString()));
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				logger.severe(file.toString() + " cannot be read");
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}
	
	private static String convertToClassName(Path src, Path javaFile) {
		String withExtension = convertToClassNameByConvention(src
				.relativize(javaFile)
				.toString());
		return withExtension.substring(0, withExtension.lastIndexOf('.'));
	}
	
	private static String convertToClassNameByConvention(String classPathFromSrc) {
		return classPathFromSrc
			.replace('/', '.')
			.replace('\\', '.');
	}

	public static ProductFeatureLocation createAndLogIfException(String javaSourcePath) {
		try {
			return new ProductFeatureLocation(javaSourcePath);
		} catch (IOException e) {
			logger.severe(e.getMessage());
			logger.severe(e.getLocalizedMessage());
		}
		return null;
	}

	public Collection<FeatureLocation> getFeatureLocationsOfClass(String classNameWithDots) {
		return productFeatureLocations.get(classNameWithDots);
	}
}
