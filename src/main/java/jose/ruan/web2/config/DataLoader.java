package jose.ruan.web2.config;

import jose.ruan.web2.dao.*;
import jose.ruan.web2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

import jose.ruan.web2.dao.*;
import jose.ruan.web2.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@Component
public class DataLoader implements CommandLineRunner {

    private final InspectionAgencyDAO inspectionAgencyDAO;
    private final DonorAgencyDAO donorAgencyDAO;
    private final ProductDAO productDAO;
    private final BatchDAO batchDAO;

    public DataLoader(InspectionAgencyDAO inspectionAgencyDAO,
                      DonorAgencyDAO donorAgencyDAO,
                      ProductDAO productDAO,
                      BatchDAO batchDAO) {
        this.inspectionAgencyDAO = inspectionAgencyDAO;
        this.donorAgencyDAO = donorAgencyDAO;
        this.productDAO = productDAO;
        this.batchDAO = batchDAO;
    }

    @Override
    public void run(String... args) throws Exception {
        InspectionAgency pf = new InspectionAgency();
        pf.setName("Polícia Federal");
        pf.setDescription("Órgão responsável por fiscalizações federais");
        int pfId = inspectionAgencyDAO.save(pf).getId();
        pf.setId(pfId);

        InspectionAgency prf = new InspectionAgency();
        prf.setName("Polícia Rodoviária Federal");
        prf.setDescription("Fiscalização nas rodovias federais");
        int prfId = inspectionAgencyDAO.save(prf).getId();
        prf.setId(prfId);

        // Criar Órgãos Donatários
        DonorAgency imip = new DonorAgency();
        imip.setName("IMIP");
        imip.setAddress("Rua da Saúde, 123");
        imip.setPhone("(81) 1234-5678");
        imip.setWorkingHours("08:00 - 17:00");
        imip.setDescription("Instituto de Medicina Integral Professor Fernando Figueira");
        int imipId = donorAgencyDAO.save(imip);
        imip.setId(imipId);

        DonorAgency apae = new DonorAgency();
        apae.setName("APAE");
        apae.setAddress("Rua das Flores, 456");
        apae.setPhone("(81) 8765-4321");
        apae.setWorkingHours("08:00 - 16:00");
        apae.setDescription("Associação de Pais e Amigos dos Excepcionais");
        int apaeId = donorAgencyDAO.save(apae);
        apae.setId(apaeId);

        // Criar Produtos
        Product prod1 = new Product();
        prod1.setName("Smartphone");
        prod1.setDescription("Celular confiscado em fiscalização");
        int prod1Id = productDAO.save(prod1);
        prod1.setCode(prod1Id);

        Product prod2 = new Product();
        prod2.setName("Tablet");
        prod2.setDescription("Tablet apreendido");
        int prod2Id = productDAO.save(prod2);
        prod2.setCode(prod2Id);

        Product prod3 = new Product();
        prod3.setName("Fones de ouvido");
        prod3.setDescription("Fones confiscados");
        int prod3Id = productDAO.save(prod3);
        prod3.setCode(prod3Id);

        Batch lote = new Batch();
        lote.setDeliveryDate(LocalDate.now());
        lote.setNotes("Primeiro lote de doação");
        lote.setInspectionAgency(pf);
        lote.setDonorAgency(imip);
        lote.setProducts(Arrays.asList(prod1, prod2, prod3));

        int batchId = batchDAO.save(lote);
        System.out.println("Lote criado com ID: " + batchId);
    }
}