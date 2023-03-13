import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Feedback, Reservation, ReservationCreateData} from '../types';
import {environment} from '../../environments/environment';
import {switchMap, take} from 'rxjs/operators';
import {BackendService} from '../backend.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationsService {

  constructor(
    private http: HttpClient,
    private backendService: BackendService
  ) { }

   list$(): Observable<Array<Reservation>> {
     return this.http.get<Array<Reservation>>(
       environment.reservationBaseUrl + '/reservations'
     );
   }

   create(reservation: ReservationCreateData): Observable<Reservation|null> {
    return this.backendService.feedback(
      this.http.post<HttpResponse<Feedback>>(
        environment.reservationBaseUrl + '/reservations',
        JSON.stringify(reservation),
        {
          headers: {
            'Content-Type': 'application/json'
          },
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( url => {
        if (url === null) {
          return of(null);
        } else {
          return this.http.get<Reservation>(url).pipe(take(1));
        }
      }),
    );
   }

   delete(reservation: Reservation): Observable<Array<Reservation>> {
    return this.backendService.feedback(
      this.http.delete<HttpResponse<Feedback>>(
        environment.reservationBaseUrl + '/reservations/' + reservation.id,
        {
          observe: 'response'
        }
      )
    ).pipe(
      switchMap( _ => {
        return this.list$();
      }),
    );
   }
}
