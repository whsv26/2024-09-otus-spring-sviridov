package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.jline.terminal.Terminal;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.shell.component.SingleItemSelector;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.support.SelectorItem;
import org.springframework.shell.style.TemplateExecutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
@RequiredArgsConstructor
public class ShellIOService implements IOService {

    private final Terminal terminal;

    private final ResourceLoader resourceLoader;

    private final TemplateExecutor templateExecutor;

    @Override
    public void printLine(String s) {
        terminal.writer().println(s);
    }

    @Override
    public String readStringWithPrompt(String prompt) {
        var stringInput = new StringInput(terminal, prompt, "");
        stringInput.setResourceLoader(resourceLoader);
        stringInput.setTemplateExecutor(templateExecutor);

        return stringInput
            .run(StringInput.StringInputContext.empty())
            .getResultValue();
    }

    @Override
    public String selectStringWithPrompt(String prompt, List<String> items) {
        var selectorItems = items.stream()
            .map(item -> SelectorItem.of(item, item))
            .toList();

        var selector = new SingleItemSelector<>(terminal, selectorItems, prompt, null);
        selector.setResourceLoader(resourceLoader);
        selector.setTemplateExecutor(templateExecutor);

        return selector
            .run(SingleItemSelector.SingleItemSelectorContext.empty())
            .getResultItem()
            .flatMap(selected -> Optional.ofNullable(selected.getItem()))
            .orElse("");
    }
}
