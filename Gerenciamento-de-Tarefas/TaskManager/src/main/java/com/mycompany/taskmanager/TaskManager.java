import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

enum TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

class Task {
    private int id;
    private String title;
    private String description;
    private String assignee;
    private TaskStatus status;
    private Date creationDate;
    private Date completionDate;

    public Task(int id, String title, String description, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = TaskStatus.PENDING;
        this.creationDate = new Date();
        this.completionDate = null;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAssignee() {
        return assignee;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public void startTask() {
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void completeTask() {
        this.status = TaskStatus.COMPLETED;
        this.completionDate = new Date(); 
    }
}

class User {
    private String username;
    private String password;
    private List<Task> assignedTasks;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.assignedTasks = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public List<Task> getAssignedTasks() {
        return assignedTasks;
    }

    public void assignTask(Task task) {
        assignedTasks.add(task);
    }
}

public class TaskManager {
    private static List<Task> tasks = new ArrayList<>();
    private static List<User> users = new ArrayList<>();

    public static void main(String[] args) {
        initializeUsers();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao sistema de gerenciamento de tarefas!");

        if (login(scanner)) {
            System.out.println("Login bem-sucedido!");

            while (true) {
                // Menu
                System.out.println("\nOpções:");
                System.out.println("1. Criar nova tarefa");
                System.out.println("2. Listar tarefas");
                System.out.println("3. Ver Minhas Tarefas");
                System.out.println("4. Marcar Tarefa como Concluída");
                System.out.println("5. Sair");
                System.out.print("Escolha uma opção: ");

                int option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        createTask(scanner);
                        break;
                    case 2:
                        listTasks();
                        break;
                    case 3:
                        viewMyTasks(scanner);
                        break;
                    case 4:
                        markTaskAsCompleted(scanner);
                        break;
                    case 5:
                        System.out.println("Saindo...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } else {
            System.out.println("Login falhou. Encerrando...");
        }

        scanner.close();
    }

    private static void initializeUsers() {
        // Usuários
        users.add(new User("admin", "admin123"));
        users.add(new User("user", "user123"));
    }

    private static boolean login(Scanner scanner) {
        System.out.print("Digite seu nome de usuário: ");
        String username = scanner.nextLine();

        System.out.print("Digite sua senha: ");
        String password = scanner.nextLine();

        // Verificação
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }

        return false; 
    }

    private static void createTask(Scanner scanner) {
        System.out.print("Digite o titulo da tarefa: ");
        String title = scanner.nextLine();

        System.out.print("Digite a descrição da tarefa: ");
        String description = scanner.nextLine();

        System.out.print("Digite o nome do responsável pela tarefa: ");
        String assignee = scanner.nextLine();

        Task newTask = new Task(tasks.size() + 1, title, description, assignee);
        tasks.add(newTask);

        // Atribuir a tarefa ao usuário atual
        User currentUser = getCurrentUser();
        currentUser.assignTask(newTask);

        System.out.println("Tarefa criada com sucesso!");
    }

    private static void listTasks() {
        System.out.println("\nLista de Tarefas:");

        for (Task task : tasks) {
            System.out.println("ID: " + task.getId());
            System.out.println("Ti­tulo: " + task.getTitle());
            System.out.println("Descrição: " + task.getDescription());
            System.out.println("Responsável: " + task.getAssignee());
            System.out.println("Data de Criação: " + task.getCreationDate());
            System.out.println("Status: " + task.getStatus());
            System.out.println("----------------------------------");
        }
    }

    private static void viewMyTasks(Scanner scanner) {
        User currentUser = getCurrentUser();
        List<Task> assignedTasks = currentUser.getAssignedTasks();

        System.out.println("\nMinhas Tarefas:");

        if (assignedTasks.isEmpty()) {
            System.out.println("Nenhuma tarefa atribuída.");
        } else {
            for (Task task : assignedTasks) {
                System.out.println("ID: " + task.getId());
                System.out.println("Título: " + task.getTitle());
                System.out.println("Descrição: " + task.getDescription());
                System.out.println("Data de Criação: " + task.getCreationDate());
                System.out.println("Status: " + task.getStatus());
                System.out.println("----------------------------------");
            }
        }
    }

    private static void markTaskAsCompleted(Scanner scanner) {
        User currentUser = getCurrentUser();
        List<Task> assignedTasks = currentUser.getAssignedTasks();

        System.out.println("\nSelecione a tarefa que deseja marcar como concluída:");

        if (assignedTasks.isEmpty()) {
            System.out.println("Nenhuma tarefa atribuída.");
            return;
        }

        System.out.println("ID\tTítulo");
        for (Task task : assignedTasks) {
            System.out.println(task.getId() + "\t" + task.getTitle());
        }

        System.out.print("Digite o ID da tarefa: ");
        int taskId = scanner.nextInt();
        scanner.nextLine();

        Task selectedTask = null;
        for (Task task : assignedTasks) {
            if (task.getId() == taskId) {
                selectedTask = task;
                break;
            }
        }

        if (selectedTask == null) {
            System.out.println("Tarefa não encontrada.");
            return;
        }

        selectedTask.completeTask();
        System.out.println("Tarefa marcada como concluída.");
    }

    private static User getCurrentUser() {
     
        return users.get(0);
    }
}