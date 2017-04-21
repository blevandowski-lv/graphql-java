package graphql.execution;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.language.Field;
import graphql.schema.GraphQLObjectType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SimpleExecutionStrategy extends ExecutionStrategy {
    @Override
    public ExecutionResult execute(ExecutionContext executionContext, GraphQLObjectType parentType, Object source, Map<String, List<Field>> fields) {
        Map<String, Object> results = new LinkedHashMap<>();
        for (String fieldName : fields.keySet()) {
            List<Field> fieldList = fields.get(fieldName);
            ExecutionResult resolvedResult = resolveField(executionContext, parentType, source, fieldList);

            results.put(fieldName, resolvedResult != null ? resolvedResult.getData() : null);

            Map<Object, Object> extensions = resolvedResult != null ? resolvedResult.getExtensions() : null;
            if(extensions != null && extensions.size() > 0)
                results.put(fieldName + " calculation", extensions.get("calculation"));
        }
        return new ExecutionResultImpl(results, executionContext.getErrors());
    }
}
