import {Component, Inject} from '@angular/core';
import {PersonData} from '../types';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-person',
  templateUrl: './person.component.html',
  styleUrls: ['./person.component.css']
})
export class PersonComponent {
  form = new FormGroup({
    username: new FormControl('', Validators.required),
    fullname: new FormControl('', Validators.required)
  });

  constructor(
    private dialogRef: MatDialogRef<PersonComponent>,
    @Inject(MAT_DIALOG_DATA) private original: PersonData
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
