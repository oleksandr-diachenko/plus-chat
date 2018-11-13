package chat.controller.settings;

import chat.component.DataDialog;
import chat.component.SettingsDialog;
import chat.model.entity.*;
import chat.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingDataController {

    private CommandRepository commandRepository;
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private SmileRepository smileRepository;
    private DirectRepository directRepository;
    private DataDialog dataDialog;
    private SettingsDialog settingsDialog;

    @Autowired
    public SettingDataController(CommandRepository commandRepository,
                                 UserRepository userRepository, RankRepository rankRepository,
                                 SmileRepository smileRepository, DirectRepository directRepository,
                                 DataDialog dataDialog, SettingsDialog settingsDialog) {
        this.commandRepository = commandRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.smileRepository = smileRepository;
        this.directRepository = directRepository;
        this.dataDialog = dataDialog;
        this.settingsDialog = settingsDialog;
    }

    public void commandsDataAction() {
        Set<Command> commands = commandRepository.getAll();
        Set<String> fields = getFields(Command.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void usersDataAction() {
        Set<User> commands = userRepository.getAll();
        Set<String> fields = getFields(User.class.getDeclaredFields());
        openDialog(new HashSet<>(commands), fields);
    }

    public void ranksDataAction() {
        Set<Rank> ranks = rankRepository.getAll();
        Set<String> fields = getFields(Rank.class.getDeclaredFields());
        openDialog(new HashSet<>(ranks), fields);
    }

    public void smilesDataAction() {
        Set<Smile> smiles = smileRepository.getAll();
        Set<String> fields = getFields(Smile.class.getDeclaredFields());
        openDialog(new HashSet<>(smiles), fields);
    }

    public void directsDataAction() {
        Set<Direct> directs = directRepository.getAll();
        Set<String> fields = getFields(Direct.class.getDeclaredFields());
        openDialog(new HashSet<>(directs), fields);
    }

    private Set<String> getFields(Field[] declaredFields) {
        Set<String> fields = new HashSet<>();
        for (Field field : declaredFields) {
            fields.add(field.getName());
        }
        return fields;
    }

    private void openDialog(Set<Object> data, Set<String> fields) {
        dataDialog.setTableFields(fields);
        dataDialog.setTableData(data);
        dataDialog.openDialog(settingsDialog.getStage());
    }

    public void reload() {
        commandRepository.getAll();
        directRepository.getAll();
        rankRepository.getAll();
        smileRepository.getAll();
        userRepository.getAll();
    }
}
