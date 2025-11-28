package tools.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;

import static com.fasterxml.jackson.annotation.JsonFormat.DEFAULT_RADIX;

public class RadixSerializerCreator {
    public static StdDeserializer<? extends Number> createRadixStringDeserializer(
            StdScalarDeserializer<? extends  Number> initialDeser,
            DeserializationContext ctxt, BeanProperty property)
    {
        JsonFormat.Value format = initialDeser.findFormatOverrides(ctxt, property, initialDeser.handledType());

        if (format == null || format.getShape() != JsonFormat.Shape.STRING) {
            return initialDeser;
        }

        if (isSerializeWithRadixOverride(format)) {
            int radix = Integer.parseInt(format.getPattern());
            return new FromStringWithRadixToNumberDeserializer(initialDeser, radix);
        }

        return initialDeser;
    }

    /**
     * Check if we have a proper {@link JsonFormat} annotation for serializing a number
     * using an alternative radix specified in the annotation.
     */
    private static boolean isSerializeWithRadixOverride(JsonFormat.Value format) {
        return format.hasNonDefaultRadix();
    }
}
