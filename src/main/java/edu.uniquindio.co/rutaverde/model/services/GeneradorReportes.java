package edu.uniquindio.co.rutaverde.model.services;
import edu.uniquindio.co.rutaverde.model.entities.*;
import java.time.LocalDate;import java.util.*;

public class GeneradorReportes {
 private final List<Reserva> reservas;

 public GeneradorReportes(List<Reserva> reservas){
     this.reservas=reservas==null?
             new ArrayList<>():reservas;
 }

 public long calcularIngresos(LocalDate inicio,LocalDate fin){
     if(inicio==null||fin==null||fin.isBefore(inicio))
         throw new IllegalArgumentException("Rango de fechas inválido");
     return reservas.stream().filter(r->"CONFIRMADA".equals(r.getEstado())&&r.getFechaReserva()!=null&&!r.getFechaReserva().toLocalDate().isBefore(inicio)&&!r.getFechaReserva().toLocalDate().isAfter(fin)).mapToLong(Reserva::getValorPagado).sum();
 }

 public Producto obtenerProductoMasVendido(){
     return reservas.stream().filter(r->"CONFIRMADA".equals(r.getEstado())&&r.getProducto()!=null).collect(java.util.stream.Collectors.groupingBy(Reserva::getProducto,java.util.stream.Collectors.summingInt(r->1))).entrySet().stream().max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse(null);
 }

 public int contarViajerosPorProducto(Producto p){
     if(p==null)return 0;return (int)reservas.stream().filter(r->"CONFIRMADA".equals(r.getEstado())&&Objects.equals(r.getProducto(),p)).count();
 }
}
