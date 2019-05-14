import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPresupuesto } from 'app/shared/model/presupuesto.model';

type EntityResponseType = HttpResponse<IPresupuesto>;
type EntityArrayResponseType = HttpResponse<IPresupuesto[]>;

@Injectable({ providedIn: 'root' })
export class PresupuestoService {
    public resourceUrl = SERVER_API_URL + 'api/presupuestos';

    constructor(protected http: HttpClient) {}

    create(presupuesto: IPresupuesto): Observable<EntityResponseType> {
        return this.http.post<IPresupuesto>(this.resourceUrl, presupuesto, { observe: 'response' });
    }

    update(presupuesto: IPresupuesto): Observable<EntityResponseType> {
        return this.http.put<IPresupuesto>(this.resourceUrl, presupuesto, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPresupuesto>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPresupuesto[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
