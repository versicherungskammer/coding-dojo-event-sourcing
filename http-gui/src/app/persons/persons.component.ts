import { Component, OnInit } from '@angular/core';
import {PersonsService} from './persons.service';
import {Person} from '../types';
import {MatDialog} from '@angular/material/dialog';
import {PersonComponent} from './person.component';
import {YesNoComponent} from '../yesno.component';

@Component({
  selector: 'app-persons',
  templateUrl: './persons.component.html',
  styleUrls: ['./persons.component.css']
})
export class PersonsComponent implements OnInit {
  list: Array<Person> = [];

  constructor(
    private service: PersonsService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.service.list$().subscribe( items => this.list = items );
  }

  create(): void {
    const dialogRef = this.dialog.open(PersonComponent, {
      data: null
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result !== null) {
        this.service.create(result).subscribe( _ => this.reload() );
      }
    });
  }

  edit(person: Person): void {
    const dialogRef = this.dialog.open(PersonComponent, {
      data: person
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result !== null) {
        this.service.update(person, result).subscribe( _ => this.reload() );
      }
    });
  }

  sick(person: Person): void {
    this.service.sick(person).subscribe( _ => this.reload() );
  }

  healthy(person: Person): void {
    this.service.healthy(person).subscribe( _ => this.reload() );
  }

  delete(person: Person): void {
    const dialogRef = this.dialog.open(YesNoComponent, {
      data: {
        title: 'Wirklich löschen?',
        message: 'Sind Sie sicher, dass Sie den/die Mitarbeiter/in ' + person.fullname + ' wirklich löschen wollen?'
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.service.delete(person, result).subscribe( _ => this.reload() );
      }
    });
  }
}
