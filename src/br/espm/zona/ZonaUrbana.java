package br.espm.zona;

import java.util.LinkedList;

import br.espm.emergencia.Emergencia;
import br.espm.sensor.Sensor;

public class ZonaUrbana extends Zona implements Emergencia {

  private LinkedList<Sensor> sensores = new LinkedList<>();

  public ZonaUrbana(String nome) {
    super(nome);
  }

  public void adicionarSensor(Sensor sensor) {
    sensores.add(sensor);
  }

  public double calcularTotal() {

    if (sensores.size() == 0) {
      throw new RuntimeException("Cadastre um sensor para efetuar o cálculo do total.");
    }

    double valoresTotais = 0;

    for (Sensor sensor : sensores) {
      valoresTotais += sensor.getValor();
    }

    return valoresTotais;
  }

  @Override
  public String classificarNivelEmergencia() {
    double media = calcularMedia();

    if (media > 300) {
      return "Alerta Vermelho (emergência total)";
    } else if (media >= 201) {
      return "Alerta Laranja";
    } else if (media >= 151) {
      return "Alerta Amarelo";
    } else if (media >= 101) {
      return "Alerta para grupos sensíveis";
    } else if (media >= 51) {
      return "Monitoramento intensificado";
    } else {
      return "Sem risco";
    }
  }

  @Override
  public String relatorio() {
    return getNome() + "Total: " + calcularTotal()
        + "\nMédia: " + calcularMedia()
        + "\nNível: " + classificarNivelEmergencia();
  }

  public double calcularMedia() {

    if (sensores.size() == 0) {
      throw new RuntimeException("Cadastre um sensor para efetuar o cálculo da média.");
    }

    double media = 0;

    for (Sensor sensor : sensores) {
      media += sensor.getValor();
    }

    return media / sensores.size();
  }

}
