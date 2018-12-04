package io.agroapp.application.web.rest;

import io.agroapp.application.AgroappApp;

import io.agroapp.application.domain.Presupuesto;
import io.agroapp.application.repository.PresupuestoRepository;
import io.agroapp.application.service.PresupuestoService;
import io.agroapp.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


import static io.agroapp.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PresupuestoResource REST controller.
 *
 * @see PresupuestoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AgroappApp.class)
public class PresupuestoResourceIntTest {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    @Autowired
    private PresupuestoRepository presupuestoRepository;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPresupuestoMockMvc;

    private Presupuesto presupuesto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PresupuestoResource presupuestoResource = new PresupuestoResource(presupuestoService);
        this.restPresupuestoMockMvc = MockMvcBuilders.standaloneSetup(presupuestoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Presupuesto createEntity(EntityManager em) {
        Presupuesto presupuesto = new Presupuesto()
            .nombre(DEFAULT_NOMBRE);
        return presupuesto;
    }

    @Before
    public void initTest() {
        presupuesto = createEntity(em);
    }

    @Test
    @Transactional
    public void createPresupuesto() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isCreated());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate + 1);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getNombre()).isEqualTo(DEFAULT_NOMBRE);
    }

    @Test
    @Transactional
    public void createPresupuestoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = presupuestoRepository.findAll().size();

        // Create the Presupuesto with an existing ID
        presupuesto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNombreIsRequired() throws Exception {
        int databaseSizeBeforeTest = presupuestoRepository.findAll().size();
        // set the field null
        presupuesto.setNombre(null);

        // Create the Presupuesto, which fails.

        restPresupuestoMockMvc.perform(post("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPresupuestos() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get all the presupuestoList
        restPresupuestoMockMvc.perform(get("/api/presupuestos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(presupuesto.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE.toString())));
    }
    
    @Test
    @Transactional
    public void getPresupuesto() throws Exception {
        // Initialize the database
        presupuestoRepository.saveAndFlush(presupuesto);

        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", presupuesto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(presupuesto.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPresupuesto() throws Exception {
        // Get the presupuesto
        restPresupuestoMockMvc.perform(get("/api/presupuestos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePresupuesto() throws Exception {
        // Initialize the database
        presupuestoService.save(presupuesto);

        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Update the presupuesto
        Presupuesto updatedPresupuesto = presupuestoRepository.findById(presupuesto.getId()).get();
        // Disconnect from session so that the updates on updatedPresupuesto are not directly saved in db
        em.detach(updatedPresupuesto);
        updatedPresupuesto
            .nombre(UPDATED_NOMBRE);

        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPresupuesto)))
            .andExpect(status().isOk());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate);
        Presupuesto testPresupuesto = presupuestoList.get(presupuestoList.size() - 1);
        assertThat(testPresupuesto.getNombre()).isEqualTo(UPDATED_NOMBRE);
    }

    @Test
    @Transactional
    public void updateNonExistingPresupuesto() throws Exception {
        int databaseSizeBeforeUpdate = presupuestoRepository.findAll().size();

        // Create the Presupuesto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPresupuestoMockMvc.perform(put("/api/presupuestos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(presupuesto)))
            .andExpect(status().isBadRequest());

        // Validate the Presupuesto in the database
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePresupuesto() throws Exception {
        // Initialize the database
        presupuestoService.save(presupuesto);

        int databaseSizeBeforeDelete = presupuestoRepository.findAll().size();

        // Get the presupuesto
        restPresupuestoMockMvc.perform(delete("/api/presupuestos/{id}", presupuesto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Presupuesto> presupuestoList = presupuestoRepository.findAll();
        assertThat(presupuestoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Presupuesto.class);
        Presupuesto presupuesto1 = new Presupuesto();
        presupuesto1.setId(1L);
        Presupuesto presupuesto2 = new Presupuesto();
        presupuesto2.setId(presupuesto1.getId());
        assertThat(presupuesto1).isEqualTo(presupuesto2);
        presupuesto2.setId(2L);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
        presupuesto1.setId(null);
        assertThat(presupuesto1).isNotEqualTo(presupuesto2);
    }
}
