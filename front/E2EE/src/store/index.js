import {createStore} from 'vuex'
import {InMemorySignalProtocolStore} from "../../public/InMemorySignalProtocolStore";
import axios from "axios";

const ls = window.libsignal;
const KeyHelper = ls.KeyHelper;
const api = axios.create({baseURL: 'http://localhost:9090'})

const arrayBufferToBase64 = (buffer) => {
    let binary = '';
    let bytes = new Uint8Array(buffer);
    let len = bytes.byteLength;
    for (let i = 0; i < len; i++) {
        binary += String.fromCharCode(bytes[i]);
    }
    return window.btoa(binary);
};

// Util import from src/helpers.js
var util = (function () {
    'use strict';

    var StaticArrayBufferProto = new ArrayBuffer().__proto__;

    return {
        toString: function (thing) {
            if (typeof thing == 'string') {
                return thing;
            }
            return new dcodeIO.ByteBuffer.wrap(thing).toString('binary');
        },
        toArrayBuffer: function (thing) {
            if (thing === undefined) {
                return undefined;
            }
            if (thing === Object(thing)) {
                if (thing.__proto__ === StaticArrayBufferProto) {
                    return thing;
                }
            }

            var str;
            if (typeof thing == "string") {
                str = thing;
            } else {
                throw new Error("Tried to convert a non-string of type " + typeof thing + " to an array buffer");
            }
            return new dcodeIO.ByteBuffer.wrap(thing, 'binary').toArrayBuffer();
        },
        isEqual: function (a, b) {
            // TODO: Special-case arraybuffers, etc
            if (a === undefined || b === undefined) {
                return false;
            }
            a = util.toString(a);
            b = util.toString(b);
            var maxLength = Math.max(a.length, b.length);
            if (maxLength < 5) {
                throw new Error("a/b compare too short");
            }
            return a.substring(0, Math.min(maxLength, a.length)) === b.substring(0, Math.min(maxLength, b.length));
        }
    };
})();

const base64ToArrayBuffer = (base64) => {
    let binary_string = window.atob(base64);
    let len = binary_string.length;
    let bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binary_string.charCodeAt(i);
    }
    return bytes.buffer;
};


const DEFAULT_NUMBER_OF_PRE_KEYS = 5;
// Generate multiple PreKeys (as per documentation)
async function generatePreKeys (registrationId) {
    let listOfPreKeysPromise = [];
    for (let i = 0; i < DEFAULT_NUMBER_OF_PRE_KEYS; i++) {
        listOfPreKeysPromise.push(KeyHelper.generatePreKey(registrationId + i + 1));
    }

    const resolvedPreKeys = await Promise.all(listOfPreKeysPromise);

    const preKeys = resolvedPreKeys.map(preKey => {
        // Create a preKey keyPair, these keys can be stored in the client
        return {
            keyId: preKey.keyId,
            keyPair: preKey.keyPair
        };
    });

    return {
        preKeys
    };
}
export default createStore({
    state: {
        userId: null,
        registrationId: null,
        identityKeyPair: null,
        signedPreKey: null,
        preKeys: null,
        // store needs to be a SignalProtocolStore impl
        // used to build sessions and handle messages
        // holds pre-keys for other users
        store: null,
    },
    getters: {
        getUserId(state) {
            return state.userId;
        },
        getRegistrationId(state){
            console.log(`getters ${state.registrationId}`);
            return state.registrationId;
        },
        getUser(state){
            return state.userId + '-' + state.registrationId;
        }
    },
    mutations: {
        ['setup-registration'] (state, [userId, registrationId, identityKeyPair, preKeys, signedPreKey]){
            console.log('setup registration')
            console.log(userId);
            console.log(registrationId);
            state.userId = parseInt(userId);
            state.registrationId = parseInt(registrationId);
            console.log(state.userId);
            console.log(state.registrationId);
            state.identityKeyPair = identityKeyPair;
            state.signedPreKey = signedPreKey;
            state.preKeys = preKeys;
            console.log(identityKeyPair);

            state.store = new InMemorySignalProtocolStore();
            state.store.put('userId',userId);
            state.store.put('registrationId', registrationId);
            state.store.put('identityKey', identityKeyPair);
            preKeys.forEach(({keyId, keyPair}) => {
                // needs to be in SignalProtocolStore
                state.store.storePreKey(keyId, keyPair);
            });
            state.store.storeSignedPreKey(signedPreKey.keyId, signedPreKey.keyPair);
            // localStorage.setItem(userId,state.store)
        },
    },
    actions: {
        async ['registration'] (context, userId) {
            console.log("inside registration")
            if(localStorage.getItem(userId) != null){
                console.log("not null")
                let localInfo = JSON.parse(localStorage.getItem(userId));
                const uid = parseInt(localInfo.userId);
                const registrationId = parseInt(localInfo.registrationId);
                let identityKeyPair = localInfo.identityKeyPair;
                identityKeyPair.pubKey = base64ToArrayBuffer(identityKeyPair.pubKey);
                identityKeyPair.privKey = base64ToArrayBuffer(identityKeyPair.privKey);
                let signedPreKey = localInfo.signedPreKey;
                signedPreKey.keyId = parseInt(signedPreKey.keyId);
                signedPreKey.keyPair.pubKey = base64ToArrayBuffer(signedPreKey.keyPair.pubKey);
                signedPreKey.keyPair.privKey = base64ToArrayBuffer(signedPreKey.keyPair.privKey);
                signedPreKey.signature = base64ToArrayBuffer(signedPreKey.signature);
                let preKeys = localInfo.preKeys.map(rawPreKey => {
                    return {
                        keyId: parseInt(rawPreKey.keyId),
                        keyPair: {
                            pubKey: base64ToArrayBuffer(rawPreKey.keyPair.pubKey),
                            privKey: base64ToArrayBuffer(rawPreKey.keyPair.privKey),
                        }
                    };
                });
                context.commit('setup-registration',
                    [uid, registrationId, identityKeyPair, preKeys, signedPreKey]);
                return {
                    registrationId: registrationId,
                    code: 400
                };
            }
            console.info(`Generating registration ID for user [${userId}]`);
            const uid = parseInt(userId);
            console.log(uid);

            const registrationId = parseInt(KeyHelper.generateRegistrationId());
            console.log(registrationId);
            const identityKeyPair = await KeyHelper.generateIdentityKeyPair();
            console.log(identityKeyPair);
            const {preKeys} = await generatePreKeys(registrationId);
            const signedPreKey = await KeyHelper.generateSignedPreKey(identityKeyPair, registrationId);
            console.log(signedPreKey);
            context.commit('setup-registration',
                [uid, registrationId, identityKeyPair, preKeys, signedPreKey]);

            return {
                registrationId: registrationId,
                code: 200
            };
        },
        async ['send-keys-to-server'] (context) {
            let reqObj = {
                userId: parseInt(context.state.userId),
                registrationId: parseInt(context.state.registrationId),
                identityKey: arrayBufferToBase64(context.state.identityKeyPair.pubKey),
                signedPreKey: {
                    keyId: parseInt(context.state.signedPreKey.keyId),
                    publicKey: arrayBufferToBase64(context.state.signedPreKey.keyPair.pubKey),
                    signature: arrayBufferToBase64(context.state.signedPreKey.signature)
                },
                preKeys: context.state.preKeys.map((preKey) => {
                    return {
                        keyId: parseInt(preKey.keyId),
                        publicKey: arrayBufferToBase64(preKey.keyPair.pubKey)
                    };
                })
            };
            console.log(reqObj);
            await api.post('/keysOf/' + context.state.userId, reqObj);
            return 'ok';
        },
        async ['get-key-bundle-of'] (context, destinationUserId){
            let httpResponse = await api.get('/keyOf/'+destinationUserId);
            return await context.dispatch('process-key', httpResponse.data);
        },
        async ['process-key'] (context, initialKeyBundle){
            console.log(initialKeyBundle);
            let userId = parseInt(initialKeyBundle.userId);
            let registrationId = parseInt(initialKeyBundle.registrationId);

            const address = new ls.SignalProtocolAddress(registrationId, userId);
            const sessionBuilder = new ls.SessionBuilder(context.state.store, address);

            let keys = initialKeyBundle;
            keys.identityKey = base64ToArrayBuffer(keys.identityKey);
            keys.signedPreKey.publicKey = base64ToArrayBuffer(keys.signedPreKey.publicKey);
            keys.signedPreKey.signature = base64ToArrayBuffer(keys.signedPreKey.signature);
            keys.preKey.publicKey = base64ToArrayBuffer(keys.preKey.publicKey);

            console.log(`Pre keys processed:`, keys);

            await sessionBuilder.processPreKey(keys);

            return true;
        },
        async ['encrypt-message'] (context, msg){
            console.log(msg);
            const address = new ls.SignalProtocolAddress(msg.destinationRegistrationId, msg.destinationUserId);
            console.log(context.state.store)
            console.log(context.state.userId)
            console.log(context.state.registrationId)
            const sessionCipher = new ls.SessionCipher(context.state.store, address);
            const ciphertext = await sessionCipher.encrypt(msg.myMsg);
            console.log(3)
            console.log(msg.destinationUserId)
            console.log(msg.destinationRegistrationId)



            let resObj = {
                groupId:msg.groupId,
                destinationUserId: msg.destinationUserId,
                destinationRegistrationId: msg.destinationRegistrationId,
                sourceUserId: context.state.userId,
                sourceRegistrationId: context.state.registrationId,
                ciphertext: ciphertext
            };
            console.log(resObj);
            return resObj;
        },
        async ['decrypt-message'] (context, msg){
            const groupId = parseInt(msg.groupId)
            const userId = parseInt(msg.sourceUserId)
            const registrationId = parseInt(msg.sourceRegistrationId);
            let fromAddress = new ls.SignalProtocolAddress(registrationId, userId);
            console.log("context.  .store:" +context.state.store)

            var temp = localStorage.getItem(msg.sourceUserId);
            // context.state.store = localStorage.getItem(msg.sourceUserId);
            let sessionCipher = new ls.SessionCipher(context.state.store, fromAddress);
            let plaintext
            if (parseInt(msg.ciphertext.type) === 3) {
                console.log(`Cipher message type 3: decryptPreKeyWhisperMessage`);
                console.log(msg.ciphertext.body)
                plaintext = await sessionCipher.decryptPreKeyWhisperMessage(msg.ciphertext.body, 'binary');
            } else if (parseInt(msg.ciphertext.type) === 1) {
                console.log(`Cipher message type 1: decryptWhisperMessage`);
                plaintext = await sessionCipher.decryptWhisperMessage(msg.ciphertext.body, 'binary');
            }
            console.log(plaintext);
            let decryptedMessage = util.toString(plaintext);
            console.log(`Decrypted message:`, decryptedMessage);
            return decryptedMessage;
        },
        async ['store-info'] (context) {
            let storeObj = {
                userId: context.state.userId,
                registrationId: context.state.registrationId,
                identityKeyPair: {
                    pubKey: arrayBufferToBase64(context.state.identityKeyPair.pubKey),
                    privKey: arrayBufferToBase64(context.state.identityKeyPair.privKey)
                },
                signedPreKey: {
                    keyId: parseInt(context.state.signedPreKey.keyId),
                    keyPair: {
                        pubKey: arrayBufferToBase64(context.state.signedPreKey.keyPair.pubKey),
                        privKey: arrayBufferToBase64(context.state.signedPreKey.keyPair.privKey),
                    },
                    signature: arrayBufferToBase64(context.state.signedPreKey.signature)
                },
                preKeys: context.state.preKeys.map((preKey) => {
                    return {
                        keyId: parseInt(preKey.keyId),
                        keyPair: {
                            pubKey: arrayBufferToBase64(preKey.keyPair.pubKey),
                            privKey: arrayBufferToBase64(preKey.keyPair.privKey),
                        }
                    };
                })
            };
            localStorage.setItem(context.state.userId, JSON.stringify(storeObj));
            return 200;
        },
        async ['check-info'] (context, userId) {
            // let resObj;
            // resObj = JSON.parse(localStorage.getItem(userId));
            // console.log(resObj.userId)
            // console.log(resObj)
            // context.state.userId = resObj.userId;
            // console.log(resObj.userId)
            // console.log(context.state.userId)
            // context.state.registrationId=resObj.registrationId;
            // context.state.identityKeyPair.pubKey=base64ToArrayBuffer(resObj.identityKeyPair.pubKey);
            // context.state.identityKeyPair.privKey=base64ToArrayBuffer(resObj.identityKeyPair.privKey);
            //
            // context.state.signedPreKey.keyId=resObj.keyId;
            // context.state.signedPreKey.keyPair.pubKey=base64ToArrayBuffer(resObj.signedPreKey.keyPair.pubKey);
            // context.state.signedPreKey.keyPair.privKey=base64ToArrayBuffer(resObj.signedPreKey.keyPair.privKey);
            // context.state.signedPreKey.signature = base64ToArrayBuffer(resObj.signedPreKey.signature);
            //
            // resObj.preKeys.map((preKey)=>{
            //     context.state.preKeys.add(preKey)
            //     }
            // )
            //
            //
            // return resObj;
            let resObj;
            resObj = JSON.parse(localStorage.getItem(userId));
            return resObj;
        },
        async ['delete-info'] (context, userId) {
            localStorage.removeItem(userId);
            return 400;
        }
    },
    modules: {}
})