package com.common.config.util;

import com.common.util.IGlossary;
import com.fasterxml.classmate.ResolvedType;
import org.springframework.stereotype.Component;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CodedEnumPropertyPlugin implements ModelPropertyBuilderPlugin {
    @Override
    public void apply(ModelPropertyContext context) {
        final Class<?> rawPrimaryType = context.getBeanPropertyDefinition().get().getRawPrimaryType();
        //过滤得到目标类型
        if (IGlossary.class.isAssignableFrom(rawPrimaryType)) {
            IGlossary[] values = (IGlossary[]) rawPrimaryType.getEnumConstants();
            final List<String> displayValues = Arrays.stream(values).map(codedEnum -> ((Enum) codedEnum).name() + ":" + ((IGlossary) codedEnum).getName()).collect(Collectors.toList());
            final AllowableListValues allowableListValues = new AllowableListValues(displayValues, rawPrimaryType.getTypeName());
            //固定设置为int类型
            final ResolvedType resolvedType = context.getResolver().resolve(String.class);
            context.getBuilder().allowableValues(allowableListValues).type(resolvedType);
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }
}