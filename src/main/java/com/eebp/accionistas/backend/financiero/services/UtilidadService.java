package com.eebp.accionistas.backend.financiero.services;

import com.eebp.accionistas.backend.accionistas.repositories.AccionistaRepository;
import com.eebp.accionistas.backend.accionistas.services.AccionistaService;
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
import java.util.List;
import java.util.Optional;

@Service
public class UtilidadService {

    @Autowired
    UtilidadRepository utilidadRepository;

    @Autowired
    AccionistaRepository accionistaRepository;

    @Autowired
    AccionistaService accionistaService;

    public Utilidad addUtilidad(Utilidad utilidad) {

        utilidadRepository.save(utilidad);
        return utilidad;
    }

    public Optional<Utilidad> findUtildadById(Integer id) {
        //Optional<Utilidad> utilidad = utilidadRepository.findById(id);
        return utilidadRepository.findById(id);
    }

    public ByteArrayInputStream excelPagoUtilidad() throws UserNotFoundException, IOException {


        String [] columns = {"ITEM", "FOLIO", "DOCUMENTO", "NOMBRES Y APELLIDO ACCIONISTAS", "Total Acciones a la Fecha", "Utilidad 100%", "Primer Pago 50%", "Segundo Pago 50%", "Observaciones"};

        Workbook workbook = new HSSFWorkbook();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        Sheet sheet = workbook.createSheet("Pago utilidad");
        CellStyle columnCellStyle = columnas(workbook);
        addHeader(sheet, workbook);


        Row row = sheet.createRow(5);

        for (int i = 0; i<columns.length; i ++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(columnCellStyle);
        }

        List<Utilidad> utilidades = utilidadRepository.findAll();
        List<AccionistasUtilidadDTO> lista = utilidadRepository.accionistasUtilidad();
        int initRow = 6;
        int item = 1;
        double totalAcciones = 0.0;
        double totalUtilidad = 0.0;
        double totalPrimerPago = 0.0;

        for (AccionistasUtilidadDTO listas : lista) {

            if ("S".equals(listas.getEsAccionista())) {
                row = sheet.createRow(initRow);
                row.createCell(0).setCellValue(item);
                row.createCell(1).setCellValue(listas.getFolioTitulo());
                row.createCell(2).setCellValue(listas.getCodAccionista());
                row.createCell(3).setCellValue(listas.getNomAccionista());


                int totalCantidadAcciones = listas.getTotalCantidadAcciones();


                Integer idUtilidad = 3;
                Optional<Utilidad> utilidadOptional = utilidadRepository.findById(idUtilidad);
                int participacionPorAccion = utilidadOptional.get().getParticipacionAccion();
                int utilidad = totalCantidadAcciones * participacionPorAccion;
                row.createCell(4).setCellValue(totalCantidadAcciones);
                row.createCell(5).setCellValue(utilidad);

                int pagoUtilidad = utilidadOptional.get().getPagoUtilidad();
                double primerPago = (utilidad*(pagoUtilidad/100.0));
                row.createCell(6).setCellValue(primerPago);

                totalAcciones += totalCantidadAcciones;
                totalUtilidad += utilidad;
                totalPrimerPago += primerPago;
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
}
