import { Component, OnInit } from '@angular/core';
import {ReservationsService} from './reservations.service';
import {Reservation, Room} from '../types';
import {MatDialog} from '@angular/material/dialog';
import {ReservationComponent} from './reservation.component';
import {YesNoComponent} from '../yesno.component';
import {PersonsService} from '../persons/persons.service';
import {combineLatest} from 'rxjs';
import {take} from 'rxjs/operators';
import {RoomsService} from '../rooms/rooms.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {
  list: Array<Reservation> = [];

  constructor(
    private service: ReservationsService,
    private roomService: RoomsService,
    private personService: PersonsService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.reload();
  }

  reload(): void {
    this.service.list$().subscribe( items => this.list = items );
  }

  create(): void {
    combineLatest([
      this.roomService.list$(),
      this.personService.list$()
    ]).pipe(
      take(1)
    ).subscribe( ([rooms, persons]) => {
      const dialogRef = this.dialog.open(ReservationComponent, {
        data: {
          rooms: rooms,
          persons: persons
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result !== null) {
          this.service.create(result).subscribe( _ => this.reload() );
        }
      });
    });
  }

  delete(reservation: Reservation): void {
    const dialogRef = this.dialog.open(YesNoComponent, {
      data: {
        title: 'Wirklich löschen?',
        message: 'Sind Sie sicher, dass Sie die Reservierung von ' + reservation.room.name + ' für ' + reservation.person.fullname + ' wirklich löschen wollen?'
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.service.delete(reservation).subscribe( _ => this.reload() );
      }
    });
  }
}
