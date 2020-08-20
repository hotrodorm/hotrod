package org.nonoptimal;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("org.nonoptimal.NonOptimal")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class NonOptimalProcessor extends AbstractProcessor {

  @Override
  public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
    for (TypeElement annotation : annotations) {
      for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
        super.processingEnv.getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING,
            "found @" + annotation + " at " + element + ". "
                + "The performance of this query is probably bad, or may become bad over time; "
                + "the database does not have the appropriate index to retrieve this data. "
                + "Consider adding a database index.");
      }
    }
    return true;
  }

}
