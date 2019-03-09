package io.microconfig.commands.buildconfig.postprocessors;

import io.microconfig.commands.buildconfig.BuildConfigPostProcessor;
import io.microconfig.configs.ConfigProvider;
import io.microconfig.configs.Property;
import io.microconfig.configs.resolver.EnvComponent;
import io.microconfig.features.secrets.SecretService;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.util.Map;
import java.util.Set;

import static io.microconfig.utils.FileUtils.delete;
import static io.microconfig.utils.Logger.announce;

@RequiredArgsConstructor
public class UpdateSecretsPostProcessor implements BuildConfigPostProcessor {
    private final SecretService secretService;

    @Override
    public void process(EnvComponent currentComponent, Map<String, Property> componentProperties,
                        File resultFile, ConfigProvider ignore) {
        try {
            doUpdate(currentComponent, componentProperties);
        } finally {
            delete(resultFile);
        }
    }

    private void doUpdate(EnvComponent currentComponent, Map<String, Property> componentProperties) {
        Set<String> update = secretService.updateSecrets(componentProperties);
        if (!update.isEmpty()) {
            announce("Appending new secrets keys: " + currentComponent.getComponent().getName() + " -> " + update);
        }
    }
}