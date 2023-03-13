import {Component, Inject, OnInit} from '@angular/core';
import {RoomsService} from './rooms.service';
import {Room, RoomData} from '../types';
import {Observable} from 'rxjs';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-room',
  templateUrl: './room.component.html',
  styleUrls: ['./room.component.css']
})
export class RoomComponent {
  form = new FormGroup({
    name: new FormControl('', Validators.required)
  });

  constructor(
    private dialogRef: MatDialogRef<RoomComponent>,
    @Inject(MAT_DIALOG_DATA) private original: RoomData
  ) {
    this.form.patchValue(original);
  }

  cancel(): void {
    this.dialogRef.close(null);
  }
  save(): void {
    this.dialogRef.close(this.form.value);
  }
}
