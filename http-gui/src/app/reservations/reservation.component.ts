import {Component, Inject, OnInit} from '@angular/core';
import {ReservationsService} from './reservations.service';
import {Person, Room, RoomData} from '../types';
import {Observable} from 'rxjs';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent {
  form = new FormGroup({
    room: new FormControl('', Validators.required),
    person: new FormControl('', Validators.required)
  });

  rooms: Array<Room> = [];
  persons: Array<Person> = [];

  constructor(
    private dialogRef: MatDialogRef<ReservationComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { rooms: Array<Room>, persons: Array<Person> }
  ) {
    this.rooms = data.rooms;
    this.persons = data.persons;
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
  save(): void {
    this.dialogRef.close(this.form.value);
  }
}
