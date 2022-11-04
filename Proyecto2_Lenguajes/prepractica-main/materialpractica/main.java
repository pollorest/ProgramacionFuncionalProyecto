import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class main {
    static List <Empleado> empleados;
    public static void main(String[] args) throws IOException {
        cargarArchivo();
        rangoSalario();
        mostrarEmpleadosPorDepartamento();
        cantidadEmpleadosDepartamento();
        sumaSalariosDepartamento();
        empleadosPorDepartamento();
        //mayorGananciaDepartamento();
        mayorSalario();
        menorSalario();
        promedioSalarioPorDepartamento();
        promedioSalarioGeneral();
        valorTotalNomina();
    }

    static void cargarArchivo() throws IOException {
        // inicializa arreglo de objetos Empleado
        Pattern patron = Pattern.compile(";"); // patron para separar entrada
        String fileName = "empleado.csv";
        try (Stream<String> lines = Files.lines(Path.of(fileName))) {
                main.empleados = lines.skip(1).map(line -> {
                    String[] arr = patron.split(line);
                    return new Empleado(arr[0], arr[1], arr[2], Double.parseDouble(arr[3]), arr[4]);
                }).collect(Collectors.toList());

                empleados.forEach(System.out::println);
        }
    }

    static Predicate<Empleado> cuatroASeisMil =
        e -> (e.getSalario() >= 4000 && e.getSalario() <= 6000);

    static void rangoSalario(){
        System.out.printf(
                "%nEmpleados que ganan $4000-$6000 mensuales ordenados por salario:%n");
        empleados.stream()
                .filter(cuatroASeisMil)
                .sorted(Comparator.comparing(Empleado::getSalario))
                .forEach(System.out::println);

    }

    static void mostrarEmpleadosPorDepartamento(){
        System.out.printf("%nEmpleados agrupados por departamento:%n");
        Map<String, List<Empleado>> MostrarEmpleadosPorDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento));
        MostrarEmpleadosPorDepartamento.forEach(
            (departamento, empleadosEnDepartamento) -> {
                System.out.println(departamento);
                empleadosEnDepartamento.forEach(
                    empleado -> System.out.printf("   %s%n", empleado));
            }
        );
    }

    static void cantidadEmpleadosDepartamento(){
        System.out.printf("%nCantidad de empleados por departamento:%n");
        Map<String, Long> cantidadEmpleadosDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento, TreeMap::new, Collectors.counting()));
        cantidadEmpleadosDepartamento.forEach(
            (departamento, cantidad) -> System.out.printf(
                "%s tiene %d empleado(s)%n", departamento, cantidad));
    }

    static void sumaSalariosDepartamento(){
        System.out.printf("%nSuma de los salarios por departamento:%n");
        Map<String, Double> sumaSalariosDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento, TreeMap::new, Collectors.summingDouble(Empleado::getSalario)));
        sumaSalariosDepartamento.forEach(
            (departamento, total) -> System.out.printf(
                "El total de salarios en el departamento de %s es $%,.2f%n",
                departamento, total));
    }

    static void empleadosPorDepartamento(){
        System.out.printf("%nEmpleado que gana más en cada departamento:%n");
        Map<String, List<Empleado>> agrupadoPorDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento));
        agrupadoPorDepartamento.forEach((departamento, empleadosEnDepartamento) -> {
            System.out.println(departamento);
            System.out.println(empleadosEnDepartamento.stream()
            .max(Comparator.comparing(Empleado::getSalario)));
        });

    }
    //Cambie el metodo
    /*static void mayorGananciaDepartamento(){
        System.out.printf("%nEmpleado que gana más en cada departamento:%n");
        Map<String, Optional<Empleado>> mayorGananciaDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento, TreeMap::new, Collectors.maxBy(Comparator.comparing(Empleado::getSalario))));
        mayorGananciaDepartamento.forEach(
            (departamento, empleado) ->
                System.out.printf("%s: %s%n", departamento, empleado.get()));    
    }*/


    static void mayorSalario(){
        System.out.printf("%nEmpleado con el mayor salario:%n%s%n",
            empleados.stream()
                .max(Comparator.comparing(Empleado::getSalario)));
    }

    static void menorSalario(){
        System.out.printf("%nEmpleado con el menor salario:%n%s%n",
            empleados.stream()
                .min(Comparator.comparing(Empleado::getSalario)));
    }

    static void promedioSalarioPorDepartamento(){
        System.out.printf("%nPromedio de salarios por departamento:%n");
        Map<String, Double> promedioSalarioPorDepartamento =
            empleados.stream()
                .collect(Collectors.groupingBy(Empleado::getDepartamento, TreeMap::new, Collectors.averagingDouble(Empleado::getSalario)));
        promedioSalarioPorDepartamento.forEach(
            (departamento, promedio) -> System.out.printf(
                "El promedio de salarios en el departamento de %s es $%,.2f%n",
                departamento, promedio));
    }

    static void promedioSalarioGeneral(){
        System.out.printf("%nPromedio de salarios de la empresa:%n$%,.2f%n",
            empleados.stream()
                .mapToDouble(Empleado::getSalario)
                .average()
                .getAsDouble());
    }

    static void valorTotalNomina(){
        System.out.printf("%nValor total de la nómina de la empresa:%n$%,.2f%n",
            empleados.stream()
                .mapToDouble(Empleado::getSalario)
                .sum());
    }
}