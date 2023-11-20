package com.vits.controller;

import java.util.List;

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
import com.vits.service.DoctorService;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
	@Autowired
	private DoctorService doctorService;

	@GetMapping
	public List<Doctor> getAllDoctors() {
		return doctorService.getAllDoctors();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Doctor> getCustomerById(@PathVariable Long id) {
		Doctor doctor = doctorService.getDoctorById(id);
		if (doctor != null) {
			return ResponseEntity.ok(doctor);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
		Doctor createdDoctor = doctorService.createDoctor(doctor);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdDoctor);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Doctor> updateCustomer(@PathVariable Long id, @RequestBody Doctor doctor) {
		Doctor updatedDoctor = doctorService.updateDoctor(id, doctor);
		if (updatedDoctor != null) {
			return ResponseEntity.ok(updatedDoctor);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
		boolean deleted = doctorService.deleteDoctor(id);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
