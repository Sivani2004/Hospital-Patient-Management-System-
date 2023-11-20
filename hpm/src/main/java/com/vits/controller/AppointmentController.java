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

import com.vits.entity.Appointment;
import com.vits.entity.Doctor;
import com.vits.entity.Patient;
import com.vits.service.AppointmentService;
import com.vits.service.DoctorService;
import com.vits.service.PatientService;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private PatientService patientService; // Inject PatientService
	@Autowired
	private DoctorService doctorService; // Inject DoctorService

	@GetMapping
	public List<Appointment> getAllAppointments() {
		return appointmentService.getAllAppointments();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
		Appointment appointment = appointmentService.getAppointmentById(id);
		if (appointment != null) {
			return ResponseEntity.ok(appointment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
	    try {
	        // Fetch patient and doctor objects from the database based on the IDs
	        Patient patient = patientService.getPatientById(appointment.getPatient().getId());
	        Doctor doctor = doctorService.getDoctorById(appointment.getDoctor().getId());

	        if (patient == null || doctor == null) {
	            // Handle invalid patient or doctor IDs
	            return ResponseEntity.badRequest().body("Invalid patient or doctor ID");
	        }

	        // Set the fetched patient and doctor objects in the appointment
	        appointment.setPatient(patient);
	        appointment.setDoctor(doctor);

	        // Save the appointment
	        Appointment savedAppointment = appointmentService.save(appointment);

	        return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
	    } catch (ConstraintViolationException e) {
	        // Log the exception for debugging purposes
	        e.printStackTrace();
	        
	        // Handle the exception, e.g., return a meaningful error response
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Foreign key constraint violation: " + e.getMessage());
	    } catch (Exception e) {
	        // Handle other exceptions as needed
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the appointment.");
	    }
	}



	@PutMapping("/{id}")
	public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
		Appointment updatedAppointment = appointmentService.updateAppointment(id, appointment);
		if (updatedAppointment != null) {
			return ResponseEntity.ok(updatedAppointment);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
		boolean deleted = appointmentService.deleteAppointment(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
