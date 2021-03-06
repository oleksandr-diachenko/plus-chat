package chat.controller.settings;

import chat.component.dialog.DataDialog;
import chat.component.dialog.SettingsDialog;
import chat.model.entity.*;
import chat.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Oleksandr_Diachenko
 */
@Controller
public class SettingDataTabController {

    private CommandRepository commandRepository;
    private UserRepository userRepository;
    private RankRepository rankRepository;
    private SmileRepository smileRepository;
    private DirectRepository directRepository;
    private OrderRepository orderRepository;
    private DataDialog dataDialog;
    private SettingsDialog settingsDialog;

    @Autowired
    public SettingDataTabController(CommandRepository commandRepository,
                                    UserRepository userRepository, RankRepository rankRepository,
                                    SmileRepository smileRepository, DirectRepository directRepository,
                                    OrderRepository orderRepository,
                                    DataDialog dataDialog, SettingsDialog settingsDialog) {
        this.commandRepository = commandRepository;
        this.userRepository = userRepository;
        this.rankRepository = rankRepository;
        this.smileRepository = smileRepository;
        this.directRepository = directRepository;
        this.orderRepository = orderRepository;
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

    public void ordersDataAction() {
        Set<Order> orders = orderRepository.getAll();
        Set<String> fields = getFields(Order.class.getDeclaredFields());
        openDialog(new HashSet<>(orders), fields);
    }

    private Set<String> getFields(Field[] declaredFields) {
        Set<String> fields = new LinkedHashSet<>();
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

    void reload() {
        commandRepository.getAll();
        directRepository.getAll();
        rankRepository.getAll();
        smileRepository.getAll();
        userRepository.getAll();
    }
}
