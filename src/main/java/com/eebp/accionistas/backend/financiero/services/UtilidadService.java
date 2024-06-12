package com.eebp.accionistas.backend.financiero.services;

import com.eebp.accionistas.backend.accionistas.entities.Persona;
import com.eebp.accionistas.backend.accionistas.repositories.AccionistaRepository;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
import com.eebp.accionistas.backend.accionistas.services.PersonaService;
import com.eebp.accionistas.backend.asamblea.entities.Asamblea;
import com.eebp.accionistas.backend.asamblea.services.AsambleaService;
import com.eebp.accionistas.backend.financiero.entities.AccionistasUtilidadDTO;
import com.eebp.accionistas.backend.financiero.entities.Utilidad;
import com.eebp.accionistas.backend.financiero.repositories.UtilidadRepository;
import com.eebp.accionistas.backend.seguridad.exceptions.UserNotFoundException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class UtilidadService {

    @Autowired
    UtilidadRepository utilidadRepository;

    @Autowired
    AccionistaRepository accionistaRepository;

    @Autowired
    AccionistaService accionistaService;

    @Autowired
    AsambleaService asambleaService;

    @Autowired
    PersonaService personaService;

    public Utilidad addUtilidad(Map<String, Object> utilidadData) {
        Utilidad utilidad = new Utilidad();

        utilidad.setNumAccMercado((Integer) utilidadData.get("numAccMercado"));
        utilidad.setNumAccUtilidades((Integer) utilidadData.get("numAccUtilidades"));
        utilidad.setParticipacionAccion((Integer) utilidadData.get("participacionAccion"));
        utilidad.setPagoUtilidad((Integer) utilidadData.get("pagoUtilidad"));

        Map<String, Integer> numPagos = (Map<String, Integer>) utilidadData.get("numPagos");
        utilidad.setPago1(numPagos.get("pago1"));
        utilidad.setPago2(numPagos.get("pago2"));
        utilidad.setPago3(numPagos.get("pago3"));

        utilidad.setValNomAccion((Integer) utilidadData.get("valNomAccion"));
        utilidad.setValIntrinseco((Integer) utilidadData.get("valIntrinseco"));
        utilidad.setDivParticipacion((Integer) utilidadData.get("divParticipacion"));

        utilidad.setFecUtilidad(new java.sql.Date(System.currentTimeMillis()));

        Integer consecutivoAsamblea = asambleaService.getConsecutivoAsamblea();
        utilidad.setConsecutivoAsamblea(consecutivoAsamblea);

        utilidadRepository.save(utilidad);
        return utilidad;
    }

    public List<Utilidad> getUtilidades(){
        return utilidadRepository.findAll();
    }

    public Optional<Utilidad> findUtildadById(Integer id) {
        //Optional<Utilidad> utilidad = utilidadRepository.findById(id);
        return utilidadRepository.findById(id);
    }

    public Utilidad getUltimaUtilidad() {
        return utilidadRepository.findFirstByOrderByIdeUtilidadDesc();
    }

    public ByteArrayInputStream excelPagoUtilidad(int anio) throws UserNotFoundException, IOException {
        String[] columns = {"ITEM", "FOLIO", "DOCUMENTO", "NOMBRES Y APELLIDO ACCIONISTAS", "Total Acciones a la Fecha", "Utilidad 100%", "Primer Pago 50%", "Observaciones"};
        List<String> columnList = new ArrayList<>(Arrays.asList(columns));

        Workbook workbook = new HSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Pago utilidad");
        CellStyle columnCellStyle = columnas(workbook);
        addHeader(sheet, workbook);

        Row row = sheet.createRow(5);

        List<Utilidad> utilidades = utilidadRepository.findByAnio(anio);

        if (!utilidades.isEmpty()) {
            Utilidad utilidad = utilidades.get(0);

            if (utilidad.getPago2() != null) {
                columnList.add(columnList.size() - 1, "Segundo Pago 50%");
            }

            if (utilidad.getPago3() != null) {
                columnList.add(columnList.size() - 1, "Tercer Pago");
            }
        }

        for (int i = 0; i < columnList.size(); i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columnList.get(i));
            cell.setCellStyle(columnCellStyle);
        }

        List<AccionistasUtilidadDTO> lista = utilidadRepository.accionistasUtilidad();
        int initRow = 6;
        int item = 1;
        double totalAcciones = 0.0;
        double totalUtilidad = 0.0;
        double totalPrimerPago = 0.0;
        double totalSegundoPago = 0.0;
        double totalTercerPago = 0.0;

        for (AccionistasUtilidadDTO listas : lista) {
            if ("S".equals(listas.getEsAccionista())) {
                row = sheet.createRow(initRow);
                row.createCell(0).setCellValue(item);
                row.createCell(1).setCellValue(listas.getFolioTitulo());
                row.createCell(2).setCellValue(listas.getCodAccionista());
                row.createCell(3).setCellValue(listas.getNomAccionista());

                int totalCantidadAcciones = listas.getTotalCantidadAcciones();

                Optional<Utilidad> utilidadOptional = utilidades.stream()
                        .findFirst();

                if (utilidadOptional.isPresent()) {
                    Utilidad utilidad = utilidadOptional.get();
                    int participacionPorAccion = utilidad.getParticipacionAccion();
                    int utilidadCalculada = totalCantidadAcciones * participacionPorAccion;
                    row.createCell(4).setCellValue(totalCantidadAcciones);
                    row.createCell(5).setCellValue(utilidadCalculada);

                    int pagoUtilidad = utilidad.getPagoUtilidad();
                    int primerPago = (int) (utilidadCalculada * (utilidad.getPago1() / 100.0));
                    row.createCell(6).setCellValue(primerPago);

                    int columnIndex = 7;

                    if (utilidad.getPago2() != null) {
                        int segundoPago = (int) (utilidadCalculada * (utilidad.getPago2() / 100.0));
                        row.createCell(columnIndex).setCellValue(segundoPago);
                        totalSegundoPago += segundoPago;
                        columnIndex++;
                    }

                    if (utilidad.getPago3() != null) {
                        int tercerPago = (int) (utilidadCalculada * (utilidad.getPago3() / 100.0));
                        row.createCell(columnIndex).setCellValue(tercerPago);
                        totalTercerPago += tercerPago;
                    }

                    totalAcciones += totalCantidadAcciones;
                    totalUtilidad += utilidadCalculada;
                    totalPrimerPago += primerPago;
                }

                initRow++;
                item++;
            }
        }

        // Agrega una fila al final con los totales
        Row totalRow = sheet.createRow(initRow);
        totalRow.createCell(3).setCellValue("Totales:");
        totalRow.createCell(4).setCellValue(totalAcciones);
        totalRow.createCell(5).setCellValue(totalUtilidad);
        totalRow.createCell(6).setCellValue(totalPrimerPago);

        int columnIndex = 7;

        if (!utilidades.isEmpty()) {
            Utilidad utilidad = utilidades.get(0);

            if (utilidad.getPago2() != null) {
                totalRow.createCell(columnIndex).setCellValue(totalSegundoPago);
                columnIndex++;
            }

            if (utilidad.getPago3() != null) {
                totalRow.createCell(columnIndex).setCellValue(totalTercerPago);
            }
        }

        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public ByteArrayInputStream generarFormato1010(int anio) throws IOException, UserNotFoundException {
        String[] columns = {
                "Tipo de documento",
                "Número identificación socio o accionista",
                "Primer apellido socio o accionista",
                "Segundo apellido socio o accionista",
                "Primer nombre del socio o accionista",
                "Otros nombres socio o accionista",
                "Razón social",
                "Dirección",
                "Código dpto.",
                "Código mcp",
                "País de residencia o domicilio",
                "Valor patrimonial acciones o aportes al 31-12",
                "Porcentaje de participación",
                "Porcentaje de participación (posición decimal)"
        };

        Workbook workbook = new HSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Formato 1010");
        CellStyle columnCellStyle = columnas(workbook);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(columnCellStyle);
        }

        List<Utilidad> utilidades = utilidadRepository.findByAnio(anio);
        List<AccionistasUtilidadDTO> accionistas = utilidadRepository.accionistasUtilidad();
        int rowNum = 1;

        if (!utilidades.isEmpty()) {
            Utilidad utilidad = utilidades.get(0);

            for (AccionistasUtilidadDTO accionista : accionistas) {
                if ("S".equals(accionista.getEsAccionista())) {
                    Optional<Persona> personaOptional = personaService.getPersona(accionista.getCodAccionista());

                    if (personaOptional.isPresent()) {
                        Persona persona = personaOptional.get();

                        Row row = sheet.createRow(rowNum++);

                        row.createCell(0).setCellValue("13");
                        row.createCell(1).setCellValue(persona.getCodUsuario());
                        row.createCell(2).setCellValue(persona.getApePri());
                        row.createCell(3).setCellValue(persona.getApeSeg());
                        row.createCell(4).setCellValue(persona.getNomPri());
                        row.createCell(5).setCellValue(persona.getNomSeg());
                        row.createCell(6).setCellValue(persona.getRazonSocial());
                        row.createCell(7).setCellValue(persona.getDirDomicilio());
                        row.createCell(8).setCellValue(persona.getDepartamentoDomicilio());
                        row.createCell(9).setCellValue(persona.getMunicipioDomicilio());
                        row.createCell(10).setCellValue("169");

                        double valorPatrimonial = (double) accionista.getTotalCantidadAcciones() * utilidad.getValNomAccion();

                        row.createCell(11).setCellValue(valorPatrimonial); // Valor patrimonial acciones o aportes al 31-12

                        Integer porcentajeParticipacion = (accionista.getTotalCantidadAcciones() * 100) / utilidad.getNumAccMercado();
                        row.createCell(12).setCellValue(porcentajeParticipacion);
                        row.createCell(13).setCellValue(porcentajeParticipacion / 100);
                    }
                }
            }
        }

        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }

    public ByteArrayInputStream generarFormato1001(int anio) throws IOException, UserNotFoundException {
        String[] columns = {
                "Concepto",
                "Tipo Documento",
                "Número de Identificación del Informado",
                "Primer apellido del informado",
                "Segundo apellido del informado",
                "Primer nombre del informado",
                "Otros nombres del informado",
                "Razon social informado",
                "Dirección",
                "Codigo Dpto",
                "Codigo Mcp",
                "Pais de residencia o domicilio",
                "Pago o abono en cuenta deducible",
                "Pago o abono en cuenta no deducible",
                "IVA mayor del costo o gasto deducible",
                "IVA mayor del costo o gasto no deducible",
                "Retencion en la fuente practicada en renta",
                "Retencion en la fuente asumida en renta",
                "Retencion en la fuente practicada IVA regimen comun",
                "Retencion en la fuente practicada IVA no domiciliados",
                "Pago o abono en cuenta deducible",
                "Pago o abono en cuenta no deducible",
                "IVA mayor del costo o gasto deducible",
                "IVA mayor del costo o gasto no deducible",
                "Retencion en la fuente practicada en renta",
                "Retencion en la fuente asumida en renta",
                "Retencion en la fuente practicada IVA regimen comun",
                "Retencion en la fuente practicada IVA no domiciliados",
                "Pago o abono en cuenta deducible",
                "Pago o abono en cuenta no deducible",
                "IVA mayor del costo o gasto deducible",
                "IVA mayor del costo o gasto no deducible",
                "Retencion en la fuente practicada en renta",
                "Retencion en la fuente asumida en renta",
                "Retencion en la fuente practicada IVA regimen comun",
                "Retencion en la fuente practicada IVA no domiciliados"
        };

        Workbook workbook = new HSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Formato Conceptos");
        CellStyle columnCellStyle = columnas(workbook);

        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(columnCellStyle);
        }

        List<Utilidad> utilidades = utilidadRepository.findByAnio(anio);
        List<AccionistasUtilidadDTO> accionistas = utilidadRepository.accionistasUtilidad();
        int rowNum = 1;

        if (!utilidades.isEmpty()) {
            Utilidad utilidad = utilidades.get(0);

            for (AccionistasUtilidadDTO accionista : accionistas) {
                if ("S".equals(accionista.getEsAccionista())) {
                    Optional<Persona> personaOptional = personaService.getPersona(accionista.getCodAccionista());

                    if (personaOptional.isPresent()) {
                        Persona persona = personaOptional.get();

                        Row row = sheet.createRow(rowNum++);

                        row.createCell(0).setCellValue("5071"); // Concepto
                        row.createCell(1).setCellValue("13");
                        row.createCell(2).setCellValue(persona.getCodUsuario());
                        row.createCell(3).setCellValue(persona.getApePri());
                        row.createCell(4).setCellValue(persona.getApeSeg());
                        row.createCell(5).setCellValue(persona.getNomPri());
                        row.createCell(6).setCellValue(persona.getNomSeg());
                        row.createCell(7).setCellValue(persona.getRazonSocial());
                        row.createCell(8).setCellValue(persona.getDirDomicilio());
                        row.createCell(9).setCellValue(persona.getDepartamentoDomicilio());
                        row.createCell(10).setCellValue(persona.getMunicipioDomicilio());
                        row.createCell(11).setCellValue("169");
                        row.createCell(12).setCellValue("-");
                        row.createCell(13).setCellValue("-");
                        row.createCell(14).setCellValue("-");
                        row.createCell(15).setCellValue("-");
                        row.createCell(16).setCellValue("-");
                        row.createCell(17).setCellValue("-");
                        row.createCell(18).setCellValue("-");
                        row.createCell(19).setCellValue("-");
                        row.createCell(20).setCellValue("-");
                        row.createCell(21).setCellValue("-");
                        row.createCell(22).setCellValue("-");
                        row.createCell(23).setCellValue("-");
                        row.createCell(24).setCellValue("-");
                        row.createCell(25).setCellValue("-");
                        row.createCell(26).setCellValue("-");
                        row.createCell(27).setCellValue("-");
                        row.createCell(28).setCellValue("-");
                        row.createCell(29).setCellValue("-");
                        row.createCell(30).setCellValue("-");
                        row.createCell(31).setCellValue("-");
                        row.createCell(32).setCellValue("-");
                        row.createCell(33).setCellValue("-");
                        row.createCell(34).setCellValue("-");
                        row.createCell(35).setCellValue("-");


                        // Aquí puedes agregar la lógica para calcular los valores de los campos restantes
                        // y asignarlos a las celdas correspondientes de la fila
                    }
                }
            }
        }

        workbook.write(stream);
        workbook.close();
        return new ByteArrayInputStream(stream.toByteArray());
    }
    private static void addHeader(Sheet sheet, Workbook workbook) throws IOException {
        // Obtener el logo desde el enlace proporcionado
        URL logoUrl = new URL("https://lh4.googleusercontent.com/Y19QN4YpCND2pRgFvwwhbJoqDjYo04OFY0FdSN3PFUbjg1GEivhR43aJ-8B36UTWPWX4bGiIIuSlygUCZOK4pvLnRtqFV6PwkAQXwdXDe7Z-jNHIR7lkJGzZPBYKnXZlWQfCvLA1fRktwH3GjDYj0Q");
        byte[] imageData = IOUtils.toByteArray(logoUrl.openStream());

        // Agregar el logo a la hoja de cálculo
        int pictureIdx = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_PNG);
        CreationHelper helper = workbook.getCreationHelper();
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = helper.createClientAnchor();

        // Establecer posición y tamaño del logo
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();


        int pictureIdxY = workbook.addPicture(imageData, Workbook.PICTURE_TYPE_PNG);
        ClientAnchor anchorY = helper.createClientAnchor();
        anchorY.setCol1(8);
        anchorY.setRow1(0);
        Picture pict2 = drawing.createPicture(anchorY, pictureIdxY);
        pict2.resize();

        // Establecer tamaño del logo (en unidades de píxeles)
        double logoWidthInPixels = 0.5;
        double logoHeightInPixels = 0.5;
        pict.resize(logoWidthInPixels, logoHeightInPixels);
        pict2.resize(logoWidthInPixels,logoHeightInPixels);

        // Establecer los datos del encabezado
        String empresa = "EMPRESA DE ENERGIA DEL BAJO PUTUMAYO SA ESP";
        String distribucion = "DISTRIBUCIÓN DE UTILIDADES AÑO 2022";
        String fecha = "MAYO DE 2023";

        // Crear fila para el encabezado
        Row headerRow = sheet.createRow(0);

        // Agregar texto del encabezado en la primera celda
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(empresa);
        headerCell.setCellStyle(getHeaderCellStyle(workbook));

        // Combinar celdas para el encabezado
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        // Crear fila para el subencabezado
        Row subHeaderRow = sheet.createRow(1);

        // Agregar texto del subencabezado en la primera celda
        Cell subHeaderCell = subHeaderRow.createCell(0);
        subHeaderCell.setCellValue(distribucion);
        subHeaderCell.setCellStyle(getHeaderCellStyle(workbook));

        // Combinar celdas para el subencabezado
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));

        // Crear fila para la fecha
        Row dateRow = sheet.createRow(2);

        // Agregar texto de la fecha en la primera celda
        Cell dateCell = dateRow.createCell(0);
        dateCell.setCellValue(fecha);
        dateCell.setCellStyle(getHeaderCellStyle(workbook));

        // Combinar celdas para la fecha
        sheet.addMergedRegion(new CellRangeAddress(2, 2, 0, 9));
    }

    private static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    public static CellStyle columnas(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex()); // Color del texto blanco
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex()); // Color de fondo azul
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }


    public ByteArrayInputStream generateMultipleFilesInZip(int anio) throws UserNotFoundException, IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);

        // Generate the Excel files
        ByteArrayInputStream excelPagoUtilidadStream = excelPagoUtilidad(anio);
        ByteArrayInputStream formato1010Stream = generarFormato1010(anio);
        ByteArrayInputStream formato1001Stream = generarFormato1001(anio);

        // Add the first Excel file to the ZIP
        addToZip(zipOutputStream, excelPagoUtilidadStream, "Pago_Utilidad_" + anio + ".xls");

        // Add the second Excel file to the ZIP
        addToZip(zipOutputStream, formato1010Stream, "Formato_1010_" + anio + ".xls");

        // Add the second Excel file to the ZIP
        addToZip(zipOutputStream, formato1001Stream, "Formato_1001_" + anio + ".xls");

        // Close the ZIP stream
        zipOutputStream.close();
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    private void addToZip(ZipOutputStream zipOutputStream, ByteArrayInputStream inputStream, String filename) throws IOException {
        ZipEntry zipEntry = new ZipEntry(filename);
        zipOutputStream.putNextEntry(zipEntry);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            zipOutputStream.write(buffer, 0, length);
        }

        zipOutputStream.closeEntry();
        inputStream.close();
    }
}
