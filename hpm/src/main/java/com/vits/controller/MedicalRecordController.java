package com.vits.controller;

import java.util.List;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vits.entity.Doctor;
import com.vits.entity.MedicalRecord;
import com.vits.entity.Patient;
import com.vits.service.DoctorService;
import com.vits.service.MedicalRecordService;
import com.vits.service.PatientService;

@RestController
@RequestMapping("/api/medicalrecords")
public class MedicalRecordController {
	@Autowired
	private MedicalRecordService medicalRecordService;
	@Autowired
	private PatientService patientService; // Inject PatientService
	@Autowired
	private DoctorService doctorService; // Inject DoctorService


	@GetMapping
	public List<MedicalRecord> getAllMedicalRecords() {
		return medicalRecordService.getAllMedicalRecords();
	}

	@GetMapping("/{id}")
	public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable Long id) {
		MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordById(id);
		if (medicalRecord != null) {
			return ResponseEntity.ok(medicalRecord);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	@PostMapping
	public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
	    try {
	        // Fetch patient and doctor objects from the database based on the IDs
	        Patient patient = patientService.getPatientById(medicalRecord.getPatient().getId());
	        Doctor doctor = doctorService.getDoctorById(medicalRecord.getDoctor().getId());

	        if (patient == null || doctor == null) {
	            // Handle invalid patient or doctor IDs by returning an error response
	            return ResponseEntity.badRequest().body(null); // Return null to indicate an error
	        }

	        // Set the fetched patient and doctor objects in the medical record
	        medicalRecord.setPatient(patient);
	        medicalRecord.setDoctor(doctor);

	        // Save the medical record
	        MedicalRecord savedMedicalRecord = medicalRecordService.save(medicalRecord);

	        return ResponseEntity.status(HttpStatus.CREATED).body(savedMedicalRecord);
	    } catch (ConstraintViolationException e) {
	        // Log the exception for debugging purposes
	        e.printStackTrace();
	        
	        // Handle the exception and return an error response with a MedicalRecord object
	        MedicalRecord errorMedicalRecord = new MedicalRecord();
	        errorMedicalRecord.setDisease("Constraint violation: " + e.getMessage()); // Set the error message
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMedicalRecord);
	    } catch (Exception e) {
	        // Handle other exceptions as needed
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Return null to indicate an error
	    }
	}


	@PutMapping("/{id}")
	public ResponseEntity<MedicalRecord> updateRental(@PathVariable Long id, @RequestBody MedicalRecord medicalRecord) {
		MedicalRecord updatedMedicalRecord = medicalRecordService.updateMedicalRecord(id, medicalRecord);
		if (updatedMedicalRecord != null) {
			return ResponseEntity.ok(updatedMedicalRecord);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
		boolean deleted = medicalRecordService.deleteMedicalRecord(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
