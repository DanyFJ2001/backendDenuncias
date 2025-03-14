package com.Denuncias.denuncias.Servicio;

import com.Denuncias.denuncias.Entidad.Denuncia;
import com.Denuncias.denuncias.Entidad.Denuncia.EstadoDenuncia;
import com.Denuncias.denuncias.Repositorio.DenunciaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.draw.LineSeparator;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DenunciaServicio {

    @Autowired
    private DenunciaRepositorio denunciaRepositorio;

    /**
     * Obtiene todas las denuncias registradas
     *
     * @return Lista de todas las denuncias
     */
    public List<Denuncia> mostrarDenuncias() {
        return denunciaRepositorio.findAll();
    }

    /**
     * Busca una denuncia por su ID
     *
     * @param id ID de la denuncia a buscar
     * @return Opcional que contiene la denuncia si existe
     */
    public Optional<Denuncia> buscarDenunciaId(Long id) {
        return denunciaRepositorio.findById(id);
    }

    /**
     * Busca denuncias por tipo
     *
     * @param tipo Tipo de denuncia a buscar
     * @return Lista de denuncias del tipo especificado
     */
    public List<Denuncia> buscarDenunciaPorTipo(String tipo) {
        return denunciaRepositorio.findByTipoContainingIgnoreCase(tipo);
    }

    /**
     * Busca denuncias por ID de usuario
     *
     * @param usuarioId ID del usuario
     * @return Lista de denuncias realizadas por el usuario
     */
    public List<Denuncia> buscarDenunciasPorUsuarioId(Long usuarioId) {
        return denunciaRepositorio.findByUsuarioId(usuarioId);
    }

    /**
     * Busca denuncias por estado
     *
     * @param estado Estado de las denuncias a buscar
     * @return Lista de denuncias en el estado especificado
     */
    public List<Denuncia> buscarDenunciasPorEstado(EstadoDenuncia estado) {
        return denunciaRepositorio.findByEstado(estado);
    }

    /**
     * Guarda o actualiza una denuncia
     *
     * @param denuncia Denuncia a guardar o actualizar
     * @return Denuncia guardada
     */
    @Transactional
    public Denuncia guardarDenuncia(Denuncia denuncia) {
        return denunciaRepositorio.save(denuncia);
    }

    /**
     * Elimina una denuncia por su ID
     *
     * @param id ID de la denuncia a eliminar
     */
    @Transactional
    public void eliminarDenuncia(Long id) {
        denunciaRepositorio.deleteById(id);
    }

    /**
     * Genera un PDF con todas las denuncias
     * @return Array de bytes con el contenido del PDF
     * @throws DocumentException Si hay un error generando el documento
     */
    /**
     * Genera un PDF con todas las denuncias con mejor diseño
     * @return Array de bytes con el contenido del PDF
     * @throws DocumentException Si hay un error generando el documento
     */
    /**
     * Este método reemplaza al anterior generarPdfDenuncias
     *
     * @return Array de bytes con el contenido del PDF
     * @throws DocumentException Si hay un error generando el documento
     */
    public byte[] generarPdfDenuncias() throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();
        addMetaData(document);

        // Título principal con estilo mejorado
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new BaseColor(24, 100, 171));
        Paragraph title = new Paragraph("LISTADO DE DENUNCIAS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(15);
        title.setSpacingAfter(20);
        document.add(title);

        // Fecha del reporte
        Font dateFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, new BaseColor(120, 144, 156));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Paragraph dateParagraph = new Paragraph("Reporte generado el " + dateFormat.format(new Date()), dateFont);
        dateParagraph.setAlignment(Element.ALIGN_RIGHT);
        dateParagraph.setSpacingAfter(15);
        document.add(dateParagraph);

        // Línea separadora
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(24, 100, 171));
        lineSeparator.setLineWidth(1.5f);
        document.add(lineSeparator);
        document.add(new Paragraph(" "));

        // Crear tabla mejorada
        PdfPTable table = new PdfPTable(6); // 6 columnas
        table.setWidthPercentage(100);

        try {
            table.setWidths(new float[]{0.5f, 1.3f, 1.5f, 1.0f, 1.0f, 1.2f});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Encabezados con mejor estilo
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
        String[] headers = {"ID", "Tipo", "Ubicación", "Estado", "Fecha", "Denunciante"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setBackgroundColor(new BaseColor(24, 100, 171));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(8);
            cell.setBorderWidth(1);
            cell.setBorderColor(new BaseColor(200, 200, 200));
            table.addCell(cell);
        }

        // Datos con filas alternadas
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Denuncia> denuncias = mostrarDenuncias();
        boolean alternarColor = false;
        BaseColor colorBorde = new BaseColor(200, 200, 200);

        for (Denuncia denuncia : denuncias) {
            alternarColor = !alternarColor;
            BaseColor rowColor = alternarColor ? new BaseColor(245, 245, 250) : BaseColor.WHITE;

            // ID
            PdfPCell cell = new PdfPCell(new Phrase(denuncia.getId().toString()));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            // Tipo
            cell = new PdfPCell(new Phrase(denuncia.getTipo()));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            table.addCell(cell);

            // Ubicación
            cell = new PdfPCell(new Phrase(denuncia.getUbicacion()));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            table.addCell(cell);

            // Estado con colores según el estado
            BaseColor estadoColor = BaseColor.BLACK;
            Font estadoFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            switch (denuncia.getEstado()) {
                case PENDIENTE:
                    estadoColor = new BaseColor(255, 152, 0); // Naranja
                    break;

                case RESUELTA:
                    estadoColor = new BaseColor(76, 175, 80); // Verde
                    break;


                case RECHAZADA:
                    estadoColor = new BaseColor(244, 67, 54); // Rojo
                    break;
            }

            estadoFont.setColor(estadoColor);
            cell = new PdfPCell(new Phrase(denuncia.getEstado().toString(), estadoFont));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            // Fecha
            cell = new PdfPCell(new Phrase(denuncia.getFechaCreacion().format(formatter)));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            // Denunciante
            cell = new PdfPCell(new Phrase(denuncia.getUsuario().getNombre()));
            cell.setPadding(6);
            cell.setBorderWidth(1);
            cell.setBorderColor(colorBorde);
            cell.setBackgroundColor(rowColor);
            table.addCell(cell);
        }

        document.add(table);

        // Nota al pie
        document.add(new Paragraph(" "));
        Font noteFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new BaseColor(120, 144, 156));
        Paragraph note = new Paragraph("Este reporte es generado automáticamente por el Sistema de Denuncias.", noteFont);
        note.setAlignment(Element.ALIGN_CENTER);
        document.add(note);

        document.close();

        return baos.toByteArray();
    }

    /**
     * Genera un PDF con el detalle de una denuncia específica
     *
     * @param id ID de la denuncia
     * @return Array de bytes con el contenido del PDF
     * @throws DocumentException Si hay un error generando el documento
     */
    public byte[] generarPdfDenunciaDetalle(Long id) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 36, 36, 54, 36);
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetaData(document);

        Optional<Denuncia> denunciaOpt = buscarDenunciaId(id);
        if (!denunciaOpt.isPresent()) {
            Font errorFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(244, 67, 54));
            Paragraph errorMsg = new Paragraph("No se encontró la denuncia con ID: " + id, errorFont);
            errorMsg.setAlignment(Element.ALIGN_CENTER);
            document.add(errorMsg);
            document.close();
            return baos.toByteArray();
        }

        Denuncia denuncia = denunciaOpt.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        // Título principal
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, new BaseColor(24, 100, 171));
        Paragraph title = new Paragraph("DETALLE DE DENUNCIA #" + denuncia.getId(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(15);
        title.setSpacingAfter(10);
        document.add(title);

        // Estado destacado
        Font estadoFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        BaseColor estadoColor = BaseColor.BLACK;

        switch (denuncia.getEstado()) {
            case PENDIENTE:
                estadoColor = new BaseColor(255, 152, 0); // Naranja
                break;

            case RESUELTA:
                estadoColor = new BaseColor(76, 175, 80); // Verde
                break;

            case RECHAZADA:
                estadoColor = new BaseColor(244, 67, 54); // Rojo
                break;
        }

        estadoFont.setColor(estadoColor);
        Paragraph estadoParagraph = new Paragraph("Estado: " + denuncia.getEstado(), estadoFont);
        estadoParagraph.setAlignment(Element.ALIGN_CENTER);
        estadoParagraph.setSpacingAfter(20);
        document.add(estadoParagraph);

        // Línea separadora
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(24, 100, 171));
        lineSeparator.setLineWidth(1.5f);
        document.add(lineSeparator);
        document.add(new Paragraph(" "));

        // Información general en formato de tabla
        Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, new BaseColor(24, 100, 171));
        document.add(new Paragraph("Información General", sectionFont));
        document.add(new Paragraph(" "));

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);

        try {
            infoTable.setWidths(new float[]{1, 3});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Estilos de celdas
        BaseColor labelBgColor = new BaseColor(240, 240, 240);
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        BaseColor colorBorde = new BaseColor(200, 200, 200);

        // Filas de información
        // Tipo
        PdfPCell labelCell = new PdfPCell(new Phrase("Tipo:", labelFont));
        labelCell.setBackgroundColor(labelBgColor);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(1);
        labelCell.setBorderColor(colorBorde);
        infoTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(denuncia.getTipo(), valueFont));
        valueCell.setPadding(6);
        valueCell.setBorderWidth(1);
        valueCell.setBorderColor(colorBorde);
        infoTable.addCell(valueCell);

        // Ubicación
        labelCell = new PdfPCell(new Phrase("Ubicación:", labelFont));
        labelCell.setBackgroundColor(labelBgColor);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(1);
        labelCell.setBorderColor(colorBorde);
        infoTable.addCell(labelCell);

        valueCell = new PdfPCell(new Phrase(denuncia.getUbicacion(), valueFont));
        valueCell.setPadding(6);
        valueCell.setBorderWidth(1);
        valueCell.setBorderColor(colorBorde);
        infoTable.addCell(valueCell);

        // Fecha Creación
        labelCell = new PdfPCell(new Phrase("Fecha Creación:", labelFont));
        labelCell.setBackgroundColor(labelBgColor);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(1);
        labelCell.setBorderColor(colorBorde);
        infoTable.addCell(labelCell);

        valueCell = new PdfPCell(new Phrase(denuncia.getFechaCreacion().format(formatter), valueFont));
        valueCell.setPadding(6);
        valueCell.setBorderWidth(1);
        valueCell.setBorderColor(colorBorde);
        infoTable.addCell(valueCell);

        // Actualización (si hay)
        if (denuncia.getFechaActualizacion() != null) {
            labelCell = new PdfPCell(new Phrase("Última Actualización:", labelFont));
            labelCell.setBackgroundColor(labelBgColor);
            labelCell.setPadding(6);
            labelCell.setBorderWidth(1);
            labelCell.setBorderColor(colorBorde);
            infoTable.addCell(labelCell);

            valueCell = new PdfPCell(new Phrase(denuncia.getFechaActualizacion().format(formatter), valueFont));
            valueCell.setPadding(6);
            valueCell.setBorderWidth(1);
            valueCell.setBorderColor(colorBorde);
            infoTable.addCell(valueCell);
        }

        document.add(infoTable);
        document.add(new Paragraph(" "));

        // Información del denunciante
        document.add(new Paragraph("Información del Denunciante", sectionFont));
        document.add(new Paragraph(" "));

        PdfPTable denuncianteTable = new PdfPTable(2);
        denuncianteTable.setWidthPercentage(100);

        try {
            denuncianteTable.setWidths(new float[]{1, 3});
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Nombre
        labelCell = new PdfPCell(new Phrase("Nombre:", labelFont));
        labelCell.setBackgroundColor(labelBgColor);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(1);
        labelCell.setBorderColor(colorBorde);
        denuncianteTable.addCell(labelCell);

        valueCell = new PdfPCell(new Phrase(denuncia.getUsuario().getNombre(), valueFont));
        valueCell.setPadding(6);
        valueCell.setBorderWidth(1);
        valueCell.setBorderColor(colorBorde);
        denuncianteTable.addCell(valueCell);

        // Contacto
        labelCell = new PdfPCell(new Phrase("Contacto:", labelFont));
        labelCell.setBackgroundColor(labelBgColor);
        labelCell.setPadding(6);
        labelCell.setBorderWidth(1);
        labelCell.setBorderColor(colorBorde);
        denuncianteTable.addCell(labelCell);

        valueCell = new PdfPCell(new Phrase(denuncia.getContacto(), valueFont));
        valueCell.setPadding(6);
        valueCell.setBorderWidth(1);
        valueCell.setBorderColor(colorBorde);
        denuncianteTable.addCell(valueCell);

        document.add(denuncianteTable);
        document.add(new Paragraph(" "));

        // Descripción
        document.add(new Paragraph("Descripción de la Denuncia", sectionFont));
        document.add(new Paragraph(" "));

        PdfPTable descTable = new PdfPTable(1);
        descTable.setWidthPercentage(100);

        PdfPCell descCell = new PdfPCell(new Phrase(denuncia.getDescripcion(), valueFont));
        descCell.setPadding(10);
        descCell.setBorderWidth(1);
        descCell.setBorderColor(colorBorde);
        descCell.setMinimumHeight(100);
        descTable.addCell(descCell);

        document.add(descTable);

        // Nota al pie
        document.add(new Paragraph(" "));
        Font noteFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, new BaseColor(120, 144, 156));
        Paragraph note = new Paragraph("Este documento es generado automáticamente por el Sistema de Denuncias y tiene validez oficial.", noteFont);
        note.setAlignment(Element.ALIGN_CENTER);
        document.add(note);

        document.close();

        return baos.toByteArray();
    }

// Métodos auxiliares necesarios

    private void addMetaData(Document document) {
        document.addTitle("Sistema de Denuncias - Reporte Oficial");
        document.addSubject("Reporte de Denuncias");
        document.addKeywords("Denuncias, Reportes, PDF, Oficial");
        document.addAuthor("Sistema de Denuncias");
        document.addCreator("Sistema de Denuncias v2.0");
    }
}