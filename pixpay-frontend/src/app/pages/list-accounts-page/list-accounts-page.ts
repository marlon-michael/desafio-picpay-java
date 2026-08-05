import { Component, inject, PLATFORM_ID, signal } from '@angular/core';
import { AccountsService } from '../../services/accounts-service';
import { isPlatformBrowser } from '@angular/common';
import { Account } from '../../models/account.model';
import { ApiResponseModel } from '../../models/api-response';

@Component({
  selector: 'app-list-accounts-page',
  imports: [],
  templateUrl: './list-accounts-page.html',
  styleUrl: './list-accounts-page.css',
})

export class ListAccountsPage {
  private accountService = inject(AccountsService);
  private platformId = inject(PLATFORM_ID);
  account = signal<Account | null>(null);
  errorMessage = signal<string | null>(null);

  ngOnInit() {
    this.findAccount();
  }

  findAccount() {
    if (isPlatformBrowser(this.platformId)) {
      this.account.set(null);
      this.errorMessage.set(null);
      this.accountService.listAccount()
        .subscribe({
          next: (data: ApiResponseModel | null) => {
            if (data == null || data.content == null) return;
            this.account.set(data.content);
          },
          error: (erro) => {
            console.log(erro, erro.status == 401)
            if (erro.status == 401) this.errorMessage.set("Credenciais não encontradas. Faça a autenticação na página inicial.");
            else this.errorMessage.set(erro.error || "Conta não disponível. Tente novamente em alguns instantes.");
          }
        });
    }
  }

}
