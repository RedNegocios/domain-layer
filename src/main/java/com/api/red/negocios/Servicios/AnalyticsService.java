package com.api.red.negocios.Servicios;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.red.negocios.DTO.AnalyticsRequestDTO;
import com.api.red.negocios.DTO.CategoriaPerformanceDTO;
import com.api.red.negocios.DTO.EmbudoConversionDTO;
import com.api.red.negocios.DTO.KPIComparativoDTO;
import com.api.red.negocios.DTO.VentasTendenciaDTO;
import com.api.red.negocios.Excepciones.EntityNotFoundException;
import com.api.red.negocios.Modelos.Negocio;
import com.api.red.negocios.Modelos.Orden;
import com.api.red.negocios.Repositorios.CategoriaRepositorio;
import com.api.red.negocios.Repositorios.NegocioRepositorio;
import com.api.red.negocios.Repositorios.OrdenRepositorio;
import com.api.red.negocios.Repositorios.UsuarioRepositorio;

@Service
public class AnalyticsService {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Autowired
    private NegocioRepositorio negocioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public VentasTendenciaDTO obtenerVentasTendencia(AnalyticsRequestDTO request) {
        // Validar que los negocios existen
        List<Negocio> negocios = new ArrayList<>();
        for (Integer negocioId : request.getNegocioIds()) {
            Negocio negocio = negocioRepositorio.findById(negocioId)
                    .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado: " + negocioId));
            negocios.add(negocio);
        }

        // Obtener órdenes de todos los negocios en el período
        List<Orden> ordenes = new ArrayList<>();
        for (Negocio negocio : negocios) {
            List<Orden> ordenesNegocio = ordenRepositorio.findByNegocioAndFechaOrdenBetween(
                    negocio, 
                    request.getFechaInicio().atStartOfDay(), 
                    request.getFechaFin().atTime(23, 59, 59)
            );
            ordenes.addAll(ordenesNegocio);
        }

        // Agrupar por período
        Map<String, List<Orden>> ordenesPorPeriodo = agruparPorPeriodoString(ordenes, request.getPeriodo());
        
        // Crear listas para cada métrica
        List<String> periodos = new ArrayList<>();
        List<BigDecimal> ventasTotales = new ArrayList<>();
        List<Integer> cantidadOrdenes = new ArrayList<>();
        List<BigDecimal> ticketPromedio = new ArrayList<>();
        List<BigDecimal> crecimientoVentas = new ArrayList<>();
        List<BigDecimal> crecimientoOrdenes = new ArrayList<>();
        
        BigDecimal ventasAnterior = BigDecimal.ZERO;
        Integer ordenesAnterior = 0;
        
        // Procesar cada período en orden
        for (String periodo : ordenesPorPeriodo.keySet().stream().sorted().collect(Collectors.toList())) {
            List<Orden> ordenesDelPeriodo = ordenesPorPeriodo.get(periodo);
            
            BigDecimal ventas = ordenesDelPeriodo.stream()
                    .map(Orden::getMontoTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                    
            Integer cantidadOrdenesActual = ordenesDelPeriodo.size();
            BigDecimal ticketPromedioActual = cantidadOrdenesActual > 0 ? 
                ventas.divide(BigDecimal.valueOf(cantidadOrdenesActual), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            
            // Calcular crecimiento
            BigDecimal crecimientoVenta = ventasAnterior.compareTo(BigDecimal.ZERO) > 0 ?
                ventas.subtract(ventasAnterior).divide(ventasAnterior, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) : BigDecimal.ZERO;
                
            BigDecimal crecimientoOrden = ordenesAnterior > 0 ?
                BigDecimal.valueOf((cantidadOrdenesActual - ordenesAnterior) * 100.0 / ordenesAnterior) : BigDecimal.ZERO;
            
            periodos.add(periodo);
            ventasTotales.add(ventas);
            cantidadOrdenes.add(cantidadOrdenesActual);
            ticketPromedio.add(ticketPromedioActual);
            crecimientoVentas.add(crecimientoVenta);
            crecimientoOrdenes.add(crecimientoOrden);
            
            ventasAnterior = ventas;
            ordenesAnterior = cantidadOrdenesActual;
        }
        
        return new VentasTendenciaDTO(periodos, ventasTotales, cantidadOrdenes, ticketPromedio, crecimientoVentas, crecimientoOrdenes);
    }

    public List<CategoriaPerformanceDTO> obtenerPerformanceCategorias(AnalyticsRequestDTO request) {
        // Validar negocios
        List<Negocio> negocios = new ArrayList<>();
        for (Integer negocioId : request.getNegocioIds()) {
            Negocio negocio = negocioRepositorio.findById(negocioId)
                    .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado: " + negocioId));
            negocios.add(negocio);
        }

        // Obtener órdenes de todos los negocios en el período
        List<Orden> ordenes = new ArrayList<>();
        for (Negocio negocio : negocios) {
            List<Orden> ordenesNegocio = ordenRepositorio.findByNegocioAndFechaOrdenBetween(
                    negocio, 
                    request.getFechaInicio().atStartOfDay(), 
                    request.getFechaFin().atTime(23, 59, 59)
            );
            ordenes.addAll(ordenesNegocio);
        }

        // Agrupar por categorías reales usando LineaOrden -> NegocioProducto -> Producto -> Categoria
        Map<String, CategoriaStats> categoriasStats = new HashMap<>();
        BigDecimal totalVentas = BigDecimal.ZERO;

        for (Orden orden : ordenes) {
            // Obtener líneas de orden para esta orden
            if (orden.getLineasOrden() != null) {
                for (var lineaOrden : orden.getLineasOrden()) {
                    if (lineaOrden.getNegocioProducto() != null && 
                        lineaOrden.getNegocioProducto().getProducto() != null) {
                        
                        var producto = lineaOrden.getNegocioProducto().getProducto();
                        String nombreCategoria = "Sin Categoría";
                        
                        if (producto.getCategoria() != null) {
                            nombreCategoria = producto.getCategoria().getNombre();
                        }
                        
                        CategoriaStats stats = categoriasStats.getOrDefault(nombreCategoria, new CategoriaStats());
                        
                        BigDecimal ventaLinea = lineaOrden.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(lineaOrden.getCantidad()));
                        
                        stats.ventas = stats.ventas.add(ventaLinea);
                        stats.unidades += lineaOrden.getCantidad();
                        
                        categoriasStats.put(nombreCategoria, stats);
                        totalVentas = totalVentas.add(ventaLinea);
                    }
                }
            } else {
                // Fallback si no hay líneas de orden cargadas
                String categoria = "Productos de " + orden.getNegocio().getNombre();
                CategoriaStats stats = categoriasStats.getOrDefault(categoria, new CategoriaStats());
                stats.ventas = stats.ventas.add(orden.getMontoTotal());
                stats.unidades += 1;
                categoriasStats.put(categoria, stats);
                totalVentas = totalVentas.add(orden.getMontoTotal());
            }
        }

        // Convertir a DTO
        List<CategoriaPerformanceDTO> resultado = new ArrayList<>();
        
        for (Map.Entry<String, CategoriaStats> entry : categoriasStats.entrySet()) {
            String categoria = entry.getKey();
            CategoriaStats stats = entry.getValue();
            
            Double participacion = totalVentas.compareTo(BigDecimal.ZERO) > 0 
                ? stats.ventas.divide(totalVentas, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue()
                : 0.0;
                
            Double crecimiento = simularCrecimiento(); // En caso real, compararías con período anterior
            
            // TODO: Actualizar constructor para nueva estructura DTO
            // resultado.add(new CategoriaPerformanceDTO(categoria, stats.ventas, crecimiento, participacion, stats.unidades));
        }
        
        return resultado.stream()
                .sorted((a, b) -> b.getVentasTotales().compareTo(a.getVentasTotales()))
                .collect(Collectors.toList());
    }

    public EmbudoConversionDTO obtenerEmbudoConversion(AnalyticsRequestDTO request) {
        // Validar negocios
        List<Negocio> negocios = new ArrayList<>();
        for (Integer negocioId : request.getNegocioIds()) {
            Negocio negocio = negocioRepositorio.findById(negocioId)
                    .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado: " + negocioId));
            negocios.add(negocio);
        }

        // Obtener órdenes completadas de todos los negocios
        List<Orden> ordenesCompletadas = new ArrayList<>();
        for (Negocio negocio : negocios) {
            List<Orden> ordenesNegocio = ordenRepositorio.findByNegocioAndFechaOrdenBetween(
                    negocio, 
                    request.getFechaInicio().atStartOfDay(), 
                    request.getFechaFin().atTime(23, 59, 59)
            );
            ordenesCompletadas.addAll(ordenesNegocio);
        }

        Integer comprasCompletadas = ordenesCompletadas.size();
        
        // Calcular embudo basado en datos reales disponibles
        // NOTA: Como no tienes tracking web real, estos son estimaciones basadas en órdenes
        Integer checkoutIniciado = Math.max((int) (comprasCompletadas * 1.2), comprasCompletadas); 
        Integer agregarCarrito = Math.max((int) (checkoutIniciado * 1.5), checkoutIniciado);
        Integer productosVistos = Math.max((int) (agregarCarrito * 2.0), agregarCarrito);
        Integer visitantes = Math.max((int) (productosVistos * 1.8), productosVistos);

        return new EmbudoConversionDTO(visitantes, productosVistos, agregarCarrito, checkoutIniciado, comprasCompletadas);
    }

    public KPIComparativoDTO obtenerKPIComparativo(AnalyticsRequestDTO request) {
        // Validar negocios
        List<Negocio> negocios = new ArrayList<>();
        for (Integer negocioId : request.getNegocioIds()) {
            Negocio negocio = negocioRepositorio.findById(negocioId)
                    .orElseThrow(() -> new EntityNotFoundException("Negocio no encontrado: " + negocioId));
            negocios.add(negocio);
        }

        // Usar fechas de comparación del request
        LocalDate inicioActual = request.getFechaInicio();
        LocalDate finActual = request.getFechaFin();
        
        LocalDate inicioAnterior = request.getFechaComparacionInicio();
        LocalDate finAnterior = request.getFechaComparacionFin();

        // Obtener datos período actual de todos los negocios
        List<Orden> ordenesActuales = new ArrayList<>();
        for (Negocio negocio : negocios) {
            List<Orden> ordenesNegocio = ordenRepositorio.findByNegocioAndFechaOrdenBetween(
                    negocio, inicioActual.atStartOfDay(), finActual.atTime(23, 59, 59)
            );
            ordenesActuales.addAll(ordenesNegocio);
        }
        
        // Obtener datos período anterior de todos los negocios
        List<Orden> ordenesAnteriores = new ArrayList<>();
        for (Negocio negocio : negocios) {
            List<Orden> ordenesNegocio = ordenRepositorio.findByNegocioAndFechaOrdenBetween(
                    negocio, inicioAnterior.atStartOfDay(), finAnterior.atTime(23, 59, 59)
            );
            ordenesAnteriores.addAll(ordenesNegocio);
        }

        // Calcular métricas actuales
        BigDecimal ventasActual = ordenesActuales.stream()
                .map(Orden::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer ordenesActualCount = ordenesActuales.size();
        Integer clientesNuevosActual = simularClientesNuevos(ordenesActualCount);

        // Calcular métricas anteriores
        BigDecimal ventasAnterior = ordenesAnteriores.stream()
                .map(Orden::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer ordenesAnteriorCount = ordenesAnteriores.size();
        Integer clientesNuevosAnterior = simularClientesNuevos(ordenesAnteriorCount);

        // Calcular crecimientos
        Double crecimientoVentas = calcularCrecimiento(ventasActual, ventasAnterior);
        Double crecimientoOrdenes = calcularCrecimiento(ordenesActualCount, ordenesAnteriorCount);
        Double crecimientoClientes = calcularCrecimiento(clientesNuevosActual, clientesNuevosAnterior);

        return new KPIComparativoDTO(
                ventasActual, ventasAnterior, crecimientoVentas,
                ordenesActualCount, ordenesAnteriorCount, crecimientoOrdenes,
                clientesNuevosActual, clientesNuevosAnterior, crecimientoClientes
        );
    }

    // Métodos auxiliares

    private Map<LocalDate, List<Orden>> agruparPorPeriodo(List<Orden> ordenes, String periodo) {
        Map<LocalDate, List<Orden>> agrupados = new HashMap<>();
        
        for (Orden orden : ordenes) {
            LocalDate fecha = orden.getFechaOrden().toLocalDate();
            LocalDate clave;
            
            switch (periodo.toLowerCase()) {
                case "semana":
                    // Agrupar por inicio de semana (lunes)
                    clave = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
                    break;
                case "mes":
                    // Agrupar por inicio de mes
                    clave = fecha.withDayOfMonth(1);
                    break;
                default: // "dia"
                    clave = fecha;
                    break;
            }
            
            agrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(orden);
        }
        
        return agrupados;
    }

    private Map<String, List<Orden>> agruparPorPeriodoString(List<Orden> ordenes, String periodo) {
        Map<String, List<Orden>> agrupados = new HashMap<>();
        
        for (Orden orden : ordenes) {
            LocalDate fecha = orden.getFechaOrden().toLocalDate();
            String clave;
            
            switch (periodo.toUpperCase()) {
                case "SEMANAL":
                    // Formato: "2024-W01"
                    int semana = fecha.get(java.time.temporal.WeekFields.ISO.weekOfWeekBasedYear());
                    clave = fecha.getYear() + "-W" + String.format("%02d", semana);
                    break;
                case "MENSUAL":
                    // Formato: "2024-01"
                    clave = fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue());
                    break;
                default: // "DIARIO"
                    // Formato: "2024-01-01"
                    clave = fecha.toString();
                    break;
            }
            
            agrupados.computeIfAbsent(clave, k -> new ArrayList<>()).add(orden);
        }
        
        return agrupados;
    }

    private Integer simularVisitantes(Integer ordenes) {
        // Simular ratio de conversión del 3-5%
        return Math.max(ordenes * 25, 100);
    }

    private Double calcularConversion(Integer ordenes, Integer visitantes) {
        if (visitantes == 0) return 0.0;
        return ((double) ordenes / visitantes) * 100;
    }

    private String simularCategoria(Integer ordenId) {
        String[] categorias = {"Electrónicos", "Ropa", "Hogar", "Deportes", "Libros", "Salud"};
        return categorias[ordenId % categorias.length];
    }

    private Integer simularUnidadesVendidas() {
        return (int) (Math.random() * 10) + 1;
    }

    private Double simularCrecimiento() {
        return (Math.random() * 40) - 10; // Entre -10% y +30%
    }

    private Integer simularClientesNuevos(Integer ordenes) {
        return (int) (ordenes * 0.7); // 70% de las órdenes son de clientes únicos
    }

    private Double calcularCrecimiento(BigDecimal actual, BigDecimal anterior) {
        if (anterior.compareTo(BigDecimal.ZERO) == 0) {
            return actual.compareTo(BigDecimal.ZERO) > 0 ? 100.0 : 0.0;
        }
        return actual.subtract(anterior)
                .divide(anterior, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    private Double calcularCrecimiento(Integer actual, Integer anterior) {
        if (anterior == 0) {
            return actual > 0 ? 100.0 : 0.0;
        }
        return ((double) (actual - anterior) / anterior) * 100;
    }

    // Clase auxiliar para estadísticas de categorías
    private static class CategoriaStats {
        public BigDecimal ventas = BigDecimal.ZERO;
        public Integer unidades = 0;
    }
}