package com.Denuncias.denuncias.Servicio;

import com.Denuncias.denuncias.Entidad.Denuncia;
import com.Denuncias.denuncias.Entidad.Denuncia.EstadoDenuncia;
import com.Denuncias.denuncias.Repositorio.DenunciaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @return Lista de todas las denuncias
     */
    public List<Denuncia> mostrarDenuncias() {
        return denunciaRepositorio.findAll();
    }

    /**
     * Busca una denuncia por su ID
     * @param id ID de la denuncia a buscar
     * @return Opcional que contiene la denuncia si existe
     */
    public Optional<Denuncia> buscarDenunciaId(Long id) {
        return denunciaRepositorio.findById(id);
    }

    /**
     * Busca denuncias por tipo
     * @param tipo Tipo de denuncia a buscar
     * @return Lista de denuncias del tipo especificado
     */
    public List<Denuncia> buscarDenunciaPorTipo(String tipo) {
        return denunciaRepositorio.findByTipoContainingIgnoreCase(tipo);
    }

    /**
     * Busca denuncias por ID de usuario
     * @param usuarioId ID del usuario
     * @return Lista de denuncias realizadas por el usuario
     */
    public List<Denuncia> buscarDenunciasPorUsuarioId(Long usuarioId) {
        return denunciaRepositorio.findByUsuarioId(usuarioId);
    }

    /**
     * Busca denuncias por estado
     * @param estado Estado de las denuncias a buscar
     * @return Lista de denuncias en el estado especificado
     */
    public List<Denuncia> buscarDenunciasPorEstado(EstadoDenuncia estado) {
        return denunciaRepositorio.findByEstado(estado);
    }

    /**
     * Guarda o actualiza una denuncia
     * @param denuncia Denuncia a guardar o actualizar
     * @return Denuncia guardada
     */
    @Transactional
    public Denuncia guardarDenuncia(Denuncia denuncia) {
        return denunciaRepositorio.save(denuncia);
    }

    /**
     * Elimina una denuncia por su ID
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
    public byte[] generarPdfDenuncias() throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetaData(document);

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        Paragraph title = new Paragraph("Listado de Denuncias", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        PdfPTable table = new PdfPTable(6); // 6 columnas
        table.setWidthPercentage(100);

        // Encabezados
        addTableHeader(table, headerFont);

        // Datos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        List<Denuncia> denuncias = mostrarDenuncias();

        for (Denuncia denuncia : denuncias) {
            table.addCell(denuncia.getId().toString());
            table.addCell(denuncia.getTipo());
            table.addCell(denuncia.getUbicacion());
            table.addCell(denuncia.getEstado().toString());
            table.addCell(denuncia.getFechaCreacion().format(formatter));
            table.addCell(denuncia.getUsuario().getNombre());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

    /**
     * Genera un PDF con el detalle de una denuncia específica
     * @param id ID de la denuncia
     * @return Array de bytes con el contenido del PDF
     * @throws DocumentException Si hay un error generando el documento
     */
    public byte[] generarPdfDenunciaDetalle(Long id) throws DocumentException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);

        document.open();
        addMetaData(document);

        Optional<Denuncia> denunciaOpt = buscarDenunciaId(id);
        if (!denunciaOpt.isPresent()) {
            document.add(new Paragraph("No se encontró la denuncia con ID: " + id));
            document.close();
            return baos.toByteArray();
        }

        Denuncia denuncia = denunciaOpt.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph title = new Paragraph("Detalle de Denuncia #" + denuncia.getId(), titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        document.add(new Paragraph("Información General", subtitleFont));
        document.add(new Paragraph("Tipo: " + denuncia.getTipo(), normalFont));
        document.add(new Paragraph("Estado: " + denuncia.getEstado(), normalFont));
        document.add(new Paragraph("Ubicación: " + denuncia.getUbicacion(), normalFont));
        document.add(new Paragraph("Fecha de Creación: " + denuncia.getFechaCreacion().format(formatter), normalFont));

        if (denuncia.getFechaActualizacion() != null) {
            document.add(new Paragraph("Última Actualización: " + denuncia.getFechaActualizacion().format(formatter), normalFont));
        }

        document.add(new Paragraph("\nInformación del Denunciante", subtitleFont));
        document.add(new Paragraph("Nombre: " + denuncia.getUsuario().getNombre(), normalFont));
        document.add(new Paragraph("Contacto: " + denuncia.getContacto(), normalFont));

        document.add(new Paragraph("\nDescripción de la Denuncia", subtitleFont));
        document.add(new Paragraph(denuncia.getDescripcion(), normalFont));

        document.close();

        return baos.toByteArray();
    }

    // Métodos privados auxiliares

    private void addMetaData(Document document) {
        document.addTitle("Sistema de Denuncias");
        document.addSubject("Reporte de Denuncias");
        document.addKeywords("Denuncias, Reportes, PDF");
        document.addAuthor("Sistema de Denuncias");
        document.addCreator("Sistema de Denuncias");
    }


    private void addTableHeader(PdfPTable table, Font headerFont) {
        String[] headers = {"ID", "Tipo", "Ubicación", "Estado", "Fecha", "Denunciante"};

        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }
    }
}