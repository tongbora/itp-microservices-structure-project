import { useEffect, useState } from "react"

interface Account {
  accountId: string;
  accountName: string;
  accountEmail: string;
}

export default function AccountProfile(){

    const [account, setAccount] = useState<Account | null>(null);
    useEffect(() => {
        fetch('/account/api/v1/accounts', {
            credentials: 'include'
        })
        .then(response => response.json())
        .then(data => setAccount(data))
        .catch(error => console.error('Error fetching account data:', error));
    }, [])


    return (
        <div>
            <h1>Account Profile Page</h1>
            {account ? (
                <div>
                    <p>Account ID: {account.accountId}</p>
                    <p>Name: {account.accountName}</p>
                    <p>Email: {account.accountEmail}</p>
                </div>
            ) : (
                <p>Loading or not authenticated</p>
            )}
        </div>
    )
}