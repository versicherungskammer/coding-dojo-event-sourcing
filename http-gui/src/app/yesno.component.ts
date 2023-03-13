import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-yesno',
  templateUrl: './yesno.component.html',
  styleUrls: ['./yesno.component.css']
})
export class YesNoComponent {
  title = 'Question';
  message = 'Are you sure?';


  constructor(
    private dialogRef: MatDialogRef<YesNoComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { message: string, title: string }
  ) {
    if (data.message) {
      this.message = data.message;
    }
    if (data.title) {
      this.title = data.title;
    }
  }

  close(yes: boolean): void {
    this.dialogRef.close(yes);
  }
}
