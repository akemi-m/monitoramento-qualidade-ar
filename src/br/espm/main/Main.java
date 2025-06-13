package br.espm.main;

import java.util.Scanner;
import java.util.TreeSet;

import br.espm.sensor.Sensor;
import br.espm.zona.Zona;
import br.espm.zona.ZonaRural;
import br.espm.zona.ZonaUrbana;

public class Main {

  private TreeSet<Zona> dadosZona = new TreeSet<>();
  private Scanner in = new Scanner(System.in);

  public void menu() {
    try {
      int opcao;

      while (true) {
        try {
          System.out.println("");
          System.out.println("1. Registrar Zona (urbana ou rural)");
          System.out.println("2. Adicionar sensor (válido apenas para zona urbana)");
          System.out.println("3. Imprimir relatório");
          System.out.println("4. Finalizar");

          opcao = in.nextInt();
          in.nextLine();

          switch (opcao) {
            case 1:
              registrarZona();
              break;
            case 2:
              adicionarSensor();
              break;
            case 3:
              imprimirRelatorio();
              break;
            case 4:
              System.out.println("Finalizando...");
              in.close();
              return;
          }

        } catch (RuntimeException e) {
          System.out.println(e.getMessage());
        }
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  private void registrarZona() {
    System.out.println("");
    System.out.println("Defina o nome da zona: ");
    String nomeZona = in.nextLine();

    if (nomeZona.equals("")) {
      System.out.println("");
      throw new RuntimeException("O campo deve ser preenchido.");
    }

    Zona zona = pesquisarZona(nomeZona);

    if (zona != null) {
      System.out.println("");
      throw new RuntimeException(nomeZona + " já está cadastrado.");
    }

    System.out.println("");
    System.out.println("Urbana ou Rural?");
    String tipoZona = in.nextLine();

    if (tipoZona.equals("")) {
      System.out.println("");
      throw new RuntimeException("O campo deve ser preenchido.");

    } else if (tipoZona.toLowerCase().equals("urbana")) {
      dadosZona.add(new ZonaUrbana(nomeZona));

    } else if (tipoZona.toLowerCase().equals("rural")) {
      dadosZona.add(new ZonaRural(nomeZona));

    } else {
      System.out.println("");
      throw new RuntimeException("Comando inválido.");
    }

    System.out.println("");
    System.out.println("Zona " + nomeZona + " cadastrada com sucesso.");
  }

  private Zona pesquisarZona(String nomeZona) {
    for (Zona zona : dadosZona) {
      if (zona.getNome().equals(nomeZona)) {
        return zona;
      }
    }
    return null;
  }

  private void adicionarSensor() {
    System.out.println("");
    System.out.println("Para adicionar um sensor, defina primeiramente o nome da zona: ");
    String nomeZona = in.nextLine();

    if (nomeZona.equals("")) {
      System.out.println("");
      throw new RuntimeException("O campo deve ser preenchido.");
    }

    Zona zona = pesquisarZona(nomeZona);

    if (zona == null) {
      System.out.println("");
      throw new RuntimeException("Efetue o cadastro da zona antes de adicionar um sensor.");
    }

    if (zona instanceof ZonaRural) {
      System.out.println("");
      throw new RuntimeException("Não há como adicionar sensores na Zona Rural.");
    }

    System.out.println("");
    System.out.println("Informe o id do sensor:");
    int id = in.nextInt();
    in.nextLine();

    System.out.println("");
    System.out.println("Informe a data da coleta:");
    String data = in.nextLine();

    System.out.println("");
    System.out.println("Informe o valor do AQI (0 a 500):");
    double valor = in.nextDouble();

    if (valor < 0 || valor > 500) {
      System.out.println("");
      throw new RuntimeException("Valor do AQI inválido. Deve estar entre 0 e 500.");
    }

    Sensor sensor = new Sensor(id, data, valor);
    ((ZonaUrbana) zona).adicionarSensor(sensor);

    System.out.println("");
    System.out.println("Sensor adicionado com sucesso!");
  }

  private void imprimirRelatorio() {
    System.out.println("");
    System.out.println("Informe o nome da zona para gerar o relatório:");
    String nomeZona = in.nextLine();

    if (nomeZona.equals("")) {
      throw new RuntimeException("O campo deve ser preenchido.");
    }

    Zona zona = pesquisarZona(nomeZona);

    if (zona == null) {
      throw new RuntimeException("Zona não encontrada.");
    }

    System.out.println("");
    System.out.println("Relatório de emergência ambiental: ");

    if (zona instanceof ZonaUrbana) {
      ZonaUrbana zonaUrbana = (ZonaUrbana) zona;

      System.out.println("");
      System.out.println("Zona: " + zonaUrbana.getNome());
      System.out.println("Total semanal: " + zonaUrbana.calcularTotal());
      System.out.println("Média semanal: " + zonaUrbana.calcularMedia());
      String nivel = zonaUrbana.classificarNivelEmergencia();
      System.out.println("Nível de emergência: " + nivel);

      if (nivel.equals("Alerta Vermelho (emergência total)")) {
        System.out.println(">>> Ação imediata recomendada: evacuação ou restrição de atividades externas.");
      }

      if (zonaUrbana.calcularMedia() > 300) {
        System.out.println(">>> ALERTA EXTREMO: Média crítica ultrapassada!");
      }

      System.out.println("");

    } else {
      System.out.println("");
      System.out.println("Zona: " + zona.getNome());
      System.out.println(zona.relatorio());
    }
  }
}
